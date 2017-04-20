package org.jhipster.health.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.health.domain.Bloodpressure;

import org.jhipster.health.repository.BloodpressureRepository;
import org.jhipster.health.repository.search.BloodpressureSearchRepository;
import org.jhipster.health.web.rest.util.HeaderUtil;
import org.jhipster.health.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Bloodpressure.
 */
@RestController
@RequestMapping("/api")
public class BloodpressureResource {

    private final Logger log = LoggerFactory.getLogger(BloodpressureResource.class);

    private static final String ENTITY_NAME = "bloodpressure";
        
    private final BloodpressureRepository bloodpressureRepository;

    private final BloodpressureSearchRepository bloodpressureSearchRepository;

    public BloodpressureResource(BloodpressureRepository bloodpressureRepository, BloodpressureSearchRepository bloodpressureSearchRepository) {
        this.bloodpressureRepository = bloodpressureRepository;
        this.bloodpressureSearchRepository = bloodpressureSearchRepository;
    }

    /**
     * POST  /bloodpressures : Create a new bloodpressure.
     *
     * @param bloodpressure the bloodpressure to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bloodpressure, or with status 400 (Bad Request) if the bloodpressure has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bloodpressures")
    @Timed
    public ResponseEntity<Bloodpressure> createBloodpressure(@Valid @RequestBody Bloodpressure bloodpressure) throws URISyntaxException {
        log.debug("REST request to save Bloodpressure : {}", bloodpressure);
        if (bloodpressure.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bloodpressure cannot already have an ID")).body(null);
        }
        Bloodpressure result = bloodpressureRepository.save(bloodpressure);
        bloodpressureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bloodpressures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bloodpressures : Updates an existing bloodpressure.
     *
     * @param bloodpressure the bloodpressure to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bloodpressure,
     * or with status 400 (Bad Request) if the bloodpressure is not valid,
     * or with status 500 (Internal Server Error) if the bloodpressure couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bloodpressures")
    @Timed
    public ResponseEntity<Bloodpressure> updateBloodpressure(@Valid @RequestBody Bloodpressure bloodpressure) throws URISyntaxException {
        log.debug("REST request to update Bloodpressure : {}", bloodpressure);
        if (bloodpressure.getId() == null) {
            return createBloodpressure(bloodpressure);
        }
        Bloodpressure result = bloodpressureRepository.save(bloodpressure);
        bloodpressureSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bloodpressure.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bloodpressures : get all the bloodpressures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bloodpressures in body
     */
    @GetMapping("/bloodpressures")
    @Timed
    public ResponseEntity<List<Bloodpressure>> getAllBloodpressures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Bloodpressures");
        Page<Bloodpressure> page = bloodpressureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bloodpressures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bloodpressures/:id : get the "id" bloodpressure.
     *
     * @param id the id of the bloodpressure to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bloodpressure, or with status 404 (Not Found)
     */
    @GetMapping("/bloodpressures/{id}")
    @Timed
    public ResponseEntity<Bloodpressure> getBloodpressure(@PathVariable Long id) {
        log.debug("REST request to get Bloodpressure : {}", id);
        Bloodpressure bloodpressure = bloodpressureRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bloodpressure));
    }

    /**
     * DELETE  /bloodpressures/:id : delete the "id" bloodpressure.
     *
     * @param id the id of the bloodpressure to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bloodpressures/{id}")
    @Timed
    public ResponseEntity<Void> deleteBloodpressure(@PathVariable Long id) {
        log.debug("REST request to delete Bloodpressure : {}", id);
        bloodpressureRepository.delete(id);
        bloodpressureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bloodpressures?query=:query : search for the bloodpressure corresponding
     * to the query.
     *
     * @param query the query of the bloodpressure search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bloodpressures")
    @Timed
    public ResponseEntity<List<Bloodpressure>> searchBloodpressures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Bloodpressures for query {}", query);
        Page<Bloodpressure> page = bloodpressureSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bloodpressures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
