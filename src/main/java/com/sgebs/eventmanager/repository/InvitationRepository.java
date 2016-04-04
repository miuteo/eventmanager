package com.sgebs.eventmanager.repository;

import com.sgebs.eventmanager.domain.Invitation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Invitation entity.
 */
public interface InvitationRepository extends JpaRepository<Invitation,Long> {

}
