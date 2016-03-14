package com.sgebs.repository;

import com.sgebs.domain.Invitation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Invitation entity.
 */
public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    @Query("select invitation from Invitation invitation where invitation.user.login = ?#{principal.username}")
    List<Invitation> findByUserIsCurrentUser();

}
