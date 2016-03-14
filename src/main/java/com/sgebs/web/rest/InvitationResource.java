package com.sgebs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sgebs.domain.Invitation;
import com.sgebs.repository.InvitationRepository;
import com.sgebs.repository.search.InvitationSearchRepository;
import com.sgebs.web.rest.util.HeaderUtil;
import com.sgebs.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Invitation.
 */
@RestController
@RequestMapping("/api")
public class InvitationResource {

    private final Logger log = LoggerFactory.getLogger(InvitationResource.class);
        
    @Inject
    private InvitationRepository invitationRepository;
    
    @Inject
    private InvitationSearchRepository invitationSearchRepository;
    
    /**
     * POST  /invitations -> Create a new invitation.
     */
    @RequestMapping(value = "/invitations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invitation> createInvitation(@Valid @RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to save Invitation : {}", invitation);
        if (invitation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invitation", "idexists", "A new invitation cannot already have an ID")).body(null);
        }
        Invitation result = invitationRepository.save(invitation);
        invitationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("invitation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invitations -> Updates an existing invitation.
     */
    @RequestMapping(value = "/invitations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invitation> updateInvitation(@Valid @RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to update Invitation : {}", invitation);
        if (invitation.getId() == null) {
            return createInvitation(invitation);
        }
        Invitation result = invitationRepository.save(invitation);
        invitationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("invitation", invitation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invitations -> get all the invitations.
     */
    @RequestMapping(value = "/invitations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Invitation>> getAllInvitations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Invitations");
        Page<Invitation> page = invitationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invitations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invitations/:id -> get the "id" invitation.
     */
    @RequestMapping(value = "/invitations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invitation> getInvitation(@PathVariable Long id) {
        log.debug("REST request to get Invitation : {}", id);
        Invitation invitation = invitationRepository.findOne(id);
        return Optional.ofNullable(invitation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invitations/:id -> delete the "id" invitation.
     */
    @RequestMapping(value = "/invitations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        log.debug("REST request to delete Invitation : {}", id);
        invitationRepository.delete(id);
        invitationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("invitation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/invitations/:query -> search for the invitation corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/invitations/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Invitation> searchInvitations(@PathVariable String query) {
        log.debug("REST request to search Invitations for query {}", query);
        return StreamSupport
            .stream(invitationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
