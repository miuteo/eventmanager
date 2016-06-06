package com.sgebs.eventmanager.repository;

import com.sgebs.eventmanager.domain.Invitation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Invitation entity.
 */
public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    @Query("select i from Invitation i where i.createdBy.login = :login")
    public List<Invitation> findCreatedByUserLogin(@Param("login") String login);

    @Query("select i from Invitation i where i.user.login = :login and i.accept is null")
    public List<Invitation> findForUserLoginNotAccepted(@Param("login") String login);

    @Query("select i from Invitation i where i.user.login = :login and i.accept is true and i.event.date between :firstDate and :secondDate")
    public List<Invitation> findAllAcceptedByDateBetweenForUserLogin(@Param("login") String login, @Param("firstDate") ZonedDateTime firstDate, @Param("secondDate") ZonedDateTime secondDate);

    @Query("select i from Invitation i where i.user.login = :login and i.accept is true")
    public List<Invitation> findAllAcceptedForUserLogin(@Param("login") String login);
}
