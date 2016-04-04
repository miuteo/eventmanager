package com.sgebs.eventmanager.service;

import com.sgebs.eventmanager.domain.Invitation;
import com.sgebs.eventmanager.repository.InvitationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Invitation.
 */
@Service
@Transactional
public class InvitationService {

    private final Logger log = LoggerFactory.getLogger(InvitationService.class);

    @Inject
    private InvitationRepository invitationRepository;

    /**
     * Save a invitation.
     *
     * @param invitation the entity to save
     * @return the persisted entity
     */
    public Invitation save(Invitation invitation) {
        log.debug("Request to save Invitation : {}", invitation);
        Invitation result = invitationRepository.save(invitation);
        return result;
    }

    /**
     *  Get all the invitations.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Invitation> findAll() {
        log.debug("Request to get all Invitations");
        List<Invitation> result = invitationRepository.findAll();
        return result;
    }

    /**
     *  Get all the invitations.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Invitation> findForUserLogin(String login) {
        log.debug("Request to get all Invitations for user [{}]", login);
        List<Invitation> result = invitationRepository.findForUserLogin(login);
        return result;
    }

    /**
     *  Get one invitation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Invitation findOne(Long id) {
        log.debug("Request to get Invitation : {}", id);
        Invitation invitation = invitationRepository.findOne(id);
        return invitation;
    }

    /**
     *  Delete the  invitation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Invitation : {}", id);
        invitationRepository.delete(id);
    }
}
