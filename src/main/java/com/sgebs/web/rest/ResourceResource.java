package com.sgebs.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sgebs.domain.Resource;
import com.sgebs.repository.ResourceRepository;
import com.sgebs.repository.search.ResourceSearchRepository;
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
 * REST controller for managing Resource.
 */
@RestController
@RequestMapping("/api")
public class ResourceResource {

    private final Logger log = LoggerFactory.getLogger(ResourceResource.class);
        
    @Inject
    private ResourceRepository resourceRepository;
    
    @Inject
    private ResourceSearchRepository resourceSearchRepository;
    
    /**
     * POST  /resources -> Create a new resource.
     */
    @RequestMapping(value = "/resources",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resource> createResource(@Valid @RequestBody Resource resource) throws URISyntaxException {
        log.debug("REST request to save Resource : {}", resource);
        if (resource.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("resource", "idexists", "A new resource cannot already have an ID")).body(null);
        }
        Resource result = resourceRepository.save(resource);
        resourceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("resource", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resources -> Updates an existing resource.
     */
    @RequestMapping(value = "/resources",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resource> updateResource(@Valid @RequestBody Resource resource) throws URISyntaxException {
        log.debug("REST request to update Resource : {}", resource);
        if (resource.getId() == null) {
            return createResource(resource);
        }
        Resource result = resourceRepository.save(resource);
        resourceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("resource", resource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resources -> get all the resources.
     */
    @RequestMapping(value = "/resources",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Resource>> getAllResources(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Resources");
        Page<Resource> page = resourceRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resources/:id -> get the "id" resource.
     */
    @RequestMapping(value = "/resources/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resource> getResource(@PathVariable Long id) {
        log.debug("REST request to get Resource : {}", id);
        Resource resource = resourceRepository.findOne(id);
        return Optional.ofNullable(resource)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /resources/:id -> delete the "id" resource.
     */
    @RequestMapping(value = "/resources/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        log.debug("REST request to delete Resource : {}", id);
        resourceRepository.delete(id);
        resourceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("resource", id.toString())).build();
    }

    /**
     * SEARCH  /_search/resources/:query -> search for the resource corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/resources/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Resource> searchResources(@PathVariable String query) {
        log.debug("REST request to search Resources for query {}", query);
        return StreamSupport
            .stream(resourceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
