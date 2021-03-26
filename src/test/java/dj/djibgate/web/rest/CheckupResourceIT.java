package dj.djibgate.web.rest;

import dj.djibgate.DjibGatesApp;
import dj.djibgate.config.TestSecurityConfiguration;
import dj.djibgate.domain.Checkup;
import dj.djibgate.repository.CheckupRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CheckupResource} REST controller.
 */
@SpringBootTest(classes = { DjibGatesApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class CheckupResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CHECK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CHECK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CheckupRepository checkupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCheckupMockMvc;

    private Checkup checkup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkup createEntity(EntityManager em) {
        Checkup checkup = new Checkup()
            .libelle(DEFAULT_LIBELLE)
            .dateCheck(DEFAULT_DATE_CHECK);
        return checkup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkup createUpdatedEntity(EntityManager em) {
        Checkup checkup = new Checkup()
            .libelle(UPDATED_LIBELLE)
            .dateCheck(UPDATED_DATE_CHECK);
        return checkup;
    }

    @BeforeEach
    public void initTest() {
        checkup = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckup() throws Exception {
        int databaseSizeBeforeCreate = checkupRepository.findAll().size();
        // Create the Checkup
        restCheckupMockMvc.perform(post("/api/checkups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkup)))
            .andExpect(status().isCreated());

        // Validate the Checkup in the database
        List<Checkup> checkupList = checkupRepository.findAll();
        assertThat(checkupList).hasSize(databaseSizeBeforeCreate + 1);
        Checkup testCheckup = checkupList.get(checkupList.size() - 1);
        assertThat(testCheckup.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCheckup.getDateCheck()).isEqualTo(DEFAULT_DATE_CHECK);
    }

    @Test
    @Transactional
    public void createCheckupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkupRepository.findAll().size();

        // Create the Checkup with an existing ID
        checkup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckupMockMvc.perform(post("/api/checkups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkup)))
            .andExpect(status().isBadRequest());

        // Validate the Checkup in the database
        List<Checkup> checkupList = checkupRepository.findAll();
        assertThat(checkupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkupRepository.findAll().size();
        // set the field null
        checkup.setLibelle(null);

        // Create the Checkup, which fails.


        restCheckupMockMvc.perform(post("/api/checkups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkup)))
            .andExpect(status().isBadRequest());

        List<Checkup> checkupList = checkupRepository.findAll();
        assertThat(checkupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCheckIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkupRepository.findAll().size();
        // set the field null
        checkup.setDateCheck(null);

        // Create the Checkup, which fails.


        restCheckupMockMvc.perform(post("/api/checkups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkup)))
            .andExpect(status().isBadRequest());

        List<Checkup> checkupList = checkupRepository.findAll();
        assertThat(checkupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCheckups() throws Exception {
        // Initialize the database
        checkupRepository.saveAndFlush(checkup);

        // Get all the checkupList
        restCheckupMockMvc.perform(get("/api/checkups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkup.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateCheck").value(hasItem(DEFAULT_DATE_CHECK.toString())));
    }
    
    @Test
    @Transactional
    public void getCheckup() throws Exception {
        // Initialize the database
        checkupRepository.saveAndFlush(checkup);

        // Get the checkup
        restCheckupMockMvc.perform(get("/api/checkups/{id}", checkup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(checkup.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.dateCheck").value(DEFAULT_DATE_CHECK.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCheckup() throws Exception {
        // Get the checkup
        restCheckupMockMvc.perform(get("/api/checkups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckup() throws Exception {
        // Initialize the database
        checkupRepository.saveAndFlush(checkup);

        int databaseSizeBeforeUpdate = checkupRepository.findAll().size();

        // Update the checkup
        Checkup updatedCheckup = checkupRepository.findById(checkup.getId()).get();
        // Disconnect from session so that the updates on updatedCheckup are not directly saved in db
        em.detach(updatedCheckup);
        updatedCheckup
            .libelle(UPDATED_LIBELLE)
            .dateCheck(UPDATED_DATE_CHECK);

        restCheckupMockMvc.perform(put("/api/checkups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCheckup)))
            .andExpect(status().isOk());

        // Validate the Checkup in the database
        List<Checkup> checkupList = checkupRepository.findAll();
        assertThat(checkupList).hasSize(databaseSizeBeforeUpdate);
        Checkup testCheckup = checkupList.get(checkupList.size() - 1);
        assertThat(testCheckup.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCheckup.getDateCheck()).isEqualTo(UPDATED_DATE_CHECK);
    }

    @Test
    @Transactional
    public void updateNonExistingCheckup() throws Exception {
        int databaseSizeBeforeUpdate = checkupRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckupMockMvc.perform(put("/api/checkups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkup)))
            .andExpect(status().isBadRequest());

        // Validate the Checkup in the database
        List<Checkup> checkupList = checkupRepository.findAll();
        assertThat(checkupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCheckup() throws Exception {
        // Initialize the database
        checkupRepository.saveAndFlush(checkup);

        int databaseSizeBeforeDelete = checkupRepository.findAll().size();

        // Delete the checkup
        restCheckupMockMvc.perform(delete("/api/checkups/{id}", checkup.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Checkup> checkupList = checkupRepository.findAll();
        assertThat(checkupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
