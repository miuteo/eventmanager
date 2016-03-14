package com.sgebs.repository;

import com.sgebs.domain.Resource;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Resource entity.
 */
public interface ResourceRepository extends JpaRepository<Resource,Long> {

}
