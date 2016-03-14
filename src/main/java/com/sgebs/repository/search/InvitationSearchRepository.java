package com.sgebs.repository.search;

import com.sgebs.domain.Invitation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Invitation entity.
 */
public interface InvitationSearchRepository extends ElasticsearchRepository<Invitation, Long> {
}
