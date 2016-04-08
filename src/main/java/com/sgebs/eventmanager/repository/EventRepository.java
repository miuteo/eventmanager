package com.sgebs.eventmanager.repository;

import com.sgebs.eventmanager.domain.Event;

import com.sgebs.eventmanager.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByCreatedByLogin(String login);
}
