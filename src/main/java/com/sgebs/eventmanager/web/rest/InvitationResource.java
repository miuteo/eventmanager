package com.sgebs.eventmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sgebs.eventmanager.domain.Invitation;
import com.sgebs.eventmanager.service.InvitationService;
import com.sgebs.eventmanager.service.UserService;
import com.sgebs.eventmanager.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Invitation.
 */
@RestController
@RequestMapping("/api")
public class InvitationResource {

    private final Logger log = LoggerFactory.getLogger(InvitationResource.class);

    @Inject
    private InvitationService invitationService;

    @Inject
    private UserService userService;

    /**
     * POST  /invitations : Create a new invitation.
     *
     * @param invitation the invitation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invitation, or with status 400 (Bad Request) if the invitation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invitations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invitation> createInvitation(@RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to save Invitation : {}", invitation);
        if (invitation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invitation", "idexists", "A new invitation cannot already have an ID")).body(null);
        }
        // NULL means not ACCEPTED or REJECTED
        invitation.setAccept(null);
        invitation.setDate( ZonedDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        invitation.setCreatedBy(userService.getUserByLogin(login));
        Invitation result = invitationService.save(invitation);

        return ResponseEntity.created(new URI("/api/invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("invitation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invitations : Updates an existing invitation.
     *
     * @param invitation the invitation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invitation,
     * or with status 400 (Bad Request) if the invitation is not valid,
     * or with status 500 (Internal Server Error) if the invitation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invitations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invitation> updateInvitation(@RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to update Invitation : {}", invitation);
        if (invitation.getId() == null) {
            return createInvitation(invitation);
        }
        Invitation result = invitationService.save(invitation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("invitation", invitation.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/acceptinvitation",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invitation> acceptInvitation(@RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to accept Invitation : {}", invitation);
        if (invitation.getId() != null) {
            Invitation existingInvitation =  invitationService.findOne(invitation.getId());
            existingInvitation.setAccept(invitation.isAccept());
            Invitation result = invitationService.save(existingInvitation);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("invitation", invitation.getId().toString()))
                .body(result);
        }
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invitation", "idexists", "You cannot accept an invitation that does not exist")).body(null);
    }

    /**
     * GET  /invitations : get all the invitations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invitations in body
     */
    @RequestMapping(value = "/invitations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Invitation> getAllInvitations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("REST request to get all Invitations visibile by user [{}] (created)", auth.getName());
        return invitationService.findCreatedByUserLogin(auth.getName());
    }

    @RequestMapping(value = "/homeinvitations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Invitation> getHomeInvitations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("REST request to get all Invitations addressed to user [{}] (invited) and not accepted", auth.getName());
        return invitationService.findForUserLoginNotAccepted(auth.getName());
    }

    /**
     * GET  /invitations/:id : get the "id" invitation.
     *
     * @param id the id of the invitation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invitation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/invitations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invitation> getInvitation(@PathVariable Long id) {
        log.debug("REST request to get Invitation : {}", id);
        Invitation invitation = invitationService.findOne(id);
        return Optional.ofNullable(invitation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invitations/:id : delete the "id" invitation.
     *
     * @param id the id of the invitation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/invitations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        log.debug("REST request to delete Invitation : {}", id);
        invitationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("invitation", id.toString())).build();
    }

}
