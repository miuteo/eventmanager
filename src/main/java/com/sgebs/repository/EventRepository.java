package com.sgebs.repository;

import com.sgebs.domain.Event;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select distinct event from Event event left join fetch event.users")
    List<Event> findAllWithEagerRelationships();

    @Query("select event from Event event left join fetch event.users where event.id =:id")
    Event findOneWithEagerRelationships(@Param("id") Long id);

}
