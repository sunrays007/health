package org.jhipster.health.repository.search;

import org.jhipster.health.domain.Bloodpressure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bloodpressure entity.
 */
public interface BloodpressureSearchRepository extends ElasticsearchRepository<Bloodpressure, Long> {
}
