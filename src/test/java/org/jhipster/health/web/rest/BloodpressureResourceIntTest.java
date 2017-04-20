package org.jhipster.health.web.rest;

import org.jhipster.health.HealthApp;

import org.jhipster.health.domain.Bloodpressure;
import org.jhipster.health.repository.BloodpressureRepository;
import org.jhipster.health.repository.search.BloodpressureSearchRepository;
import org.jhipster.health.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BloodpressureResource REST controller.
 *
 * @see BloodpressureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HealthApp.class)
public class BloodpressureResourceIntTest {

    private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SYSTOLIC = 1;
    private static final Integer UPDATED_SYSTOLIC = 2;

    private static final Integer DEFAULT_DIASTOLIC = 1;
    private static final Integer UPDATED_DIASTOLIC = 2;

    @Autowired
    private BloodpressureRepository bloodpressureRepository;

    @Autowired
    private BloodpressureSearchRepository bloodpressureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBloodpressureMockMvc;

    private Bloodpressure bloodpressure;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BloodpressureResource bloodpressureResource = new BloodpressureResource(bloodpressureRepository, bloodpressureSearchRepository);
        this.restBloodpressureMockMvc = MockMvcBuilders.standaloneSetup(bloodpressureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bloodpressure createEntity(EntityManager em) {
        Bloodpressure bloodpressure = new Bloodpressure()
            .timestamp(DEFAULT_TIMESTAMP)
            .systolic(DEFAULT_SYSTOLIC)
            .diastolic(DEFAULT_DIASTOLIC);
        return bloodpressure;
    }

    @Before
    public void initTest() {
        bloodpressureSearchRepository.deleteAll();
        bloodpressure = createEntity(em);
    }

    @Test
    @Transactional
    public void createBloodpressure() throws Exception {
        int databaseSizeBeforeCreate = bloodpressureRepository.findAll().size();

        // Create the Bloodpressure
        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isCreated());

        // Validate the Bloodpressure in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeCreate + 1);
        Bloodpressure testBloodpressure = bloodpressureList.get(bloodpressureList.size() - 1);
        assertThat(testBloodpressure.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testBloodpressure.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testBloodpressure.getDiastolic()).isEqualTo(DEFAULT_DIASTOLIC);

        // Validate the Bloodpressure in Elasticsearch
        Bloodpressure bloodpressureEs = bloodpressureSearchRepository.findOne(testBloodpressure.getId());
        assertThat(bloodpressureEs).isEqualToComparingFieldByField(testBloodpressure);
    }

    @Test
    @Transactional
    public void createBloodpressureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bloodpressureRepository.findAll().size();

        // Create the Bloodpressure with an existing ID
        bloodpressure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodpressureRepository.findAll().size();
        // set the field null
        bloodpressure.setTimestamp(null);

        // Create the Bloodpressure, which fails.

        restBloodpressureMockMvc.perform(post("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isBadRequest());

        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBloodpressures() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList
        restBloodpressureMockMvc.perform(get("/api/bloodpressures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodpressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    @Test
    @Transactional
    public void getBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get the bloodpressure
        restBloodpressureMockMvc.perform(get("/api/bloodpressures/{id}", bloodpressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bloodpressure.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC));
    }

    @Test
    @Transactional
    public void getNonExistingBloodpressure() throws Exception {
        // Get the bloodpressure
        restBloodpressureMockMvc.perform(get("/api/bloodpressures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);
        int databaseSizeBeforeUpdate = bloodpressureRepository.findAll().size();

        // Update the bloodpressure
        Bloodpressure updatedBloodpressure = bloodpressureRepository.findOne(bloodpressure.getId());
        updatedBloodpressure
            .timestamp(UPDATED_TIMESTAMP)
            .systolic(UPDATED_SYSTOLIC)
            .diastolic(UPDATED_DIASTOLIC);

        restBloodpressureMockMvc.perform(put("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBloodpressure)))
            .andExpect(status().isOk());

        // Validate the Bloodpressure in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeUpdate);
        Bloodpressure testBloodpressure = bloodpressureList.get(bloodpressureList.size() - 1);
        assertThat(testBloodpressure.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testBloodpressure.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testBloodpressure.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);

        // Validate the Bloodpressure in Elasticsearch
        Bloodpressure bloodpressureEs = bloodpressureSearchRepository.findOne(testBloodpressure.getId());
        assertThat(bloodpressureEs).isEqualToComparingFieldByField(testBloodpressure);
    }

    @Test
    @Transactional
    public void updateNonExistingBloodpressure() throws Exception {
        int databaseSizeBeforeUpdate = bloodpressureRepository.findAll().size();

        // Create the Bloodpressure

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBloodpressureMockMvc.perform(put("/api/bloodpressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bloodpressure)))
            .andExpect(status().isCreated());

        // Validate the Bloodpressure in the database
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);
        int databaseSizeBeforeDelete = bloodpressureRepository.findAll().size();

        // Get the bloodpressure
        restBloodpressureMockMvc.perform(delete("/api/bloodpressures/{id}", bloodpressure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bloodpressureExistsInEs = bloodpressureSearchRepository.exists(bloodpressure.getId());
        assertThat(bloodpressureExistsInEs).isFalse();

        // Validate the database is empty
        List<Bloodpressure> bloodpressureList = bloodpressureRepository.findAll();
        assertThat(bloodpressureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBloodpressure() throws Exception {
        // Initialize the database
        bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);

        // Search the bloodpressure
        restBloodpressureMockMvc.perform(get("/api/_search/bloodpressures?query=id:" + bloodpressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodpressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bloodpressure.class);
    }
}
