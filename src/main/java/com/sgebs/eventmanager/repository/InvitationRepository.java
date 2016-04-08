package com.sgebs.eventmanager.repository;

import com.sgebs.eventmanager.domain.Invitation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Invitation entity.
 */
public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    @Query("select i from Invitation i where i.user.login = :login or i.createdBy.login = :login")
    public List<Invitation> findForUserLogin(@Param("login") String login);
}
