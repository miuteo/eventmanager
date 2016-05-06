package com.sgebs.eventmanager.repository;

import com.sgebs.eventmanager.domain.Location;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Location entity.
 */
public interface LocationRepository extends JpaRepository<Location,Long> {

    @Query("select location from Location location where location.user.login = ?#{principal.username}")
    List<Location> findByUserIsCurrentUser();

}
