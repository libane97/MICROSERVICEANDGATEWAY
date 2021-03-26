package dj.djibgate.web.rest;

import dj.djibgate.DjibGatesApp;
import dj.djibgate.config.TestSecurityConfiguration;
import dj.djibgate.domain.Checkpoints;
import dj.djibgate.repository.CheckpointsRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CheckpointsResource} REST controller.
 */
@SpringBootTest(classes = { DjibGatesApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class CheckpointsResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOTE = 1;
    private static final Integer UPDATED_NOTE = 2;

    @Autowired
    private CheckpointsRepository checkpointsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCheckpointsMockMvc;

    private Checkpoints checkpoints;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkpoints createEntity(EntityManager em) {
        Checkpoints checkpoints = new Checkpoints()
            .libelle(DEFAULT_LIBELLE)
            .note(DEFAULT_NOTE);
        return checkpoints;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkpoints createUpdatedEntity(EntityManager em) {
        Checkpoints checkpoints = new Checkpoints()
            .libelle(UPDATED_LIBELLE)
            .note(UPDATED_NOTE);
        return checkpoints;
    }

    @BeforeEach
    public void initTest() {
        checkpoints = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckpoints() throws Exception {
        int databaseSizeBeforeCreate = checkpointsRepository.findAll().size();
        // Create the Checkpoints
        restCheckpointsMockMvc.perform(post("/api/checkpoints").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoints)))
            .andExpect(status().isCreated());

        // Validate the Checkpoints in the database
        List<Checkpoints> checkpointsList = checkpointsRepository.findAll();
        assertThat(checkpointsList).hasSize(databaseSizeBeforeCreate + 1);
        Checkpoints testCheckpoints = checkpointsList.get(checkpointsList.size() - 1);
        assertThat(testCheckpoints.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCheckpoints.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createCheckpointsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkpointsRepository.findAll().size();

        // Create the Checkpoints with an existing ID
        checkpoints.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckpointsMockMvc.perform(post("/api/checkpoints").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoints)))
            .andExpect(status().isBadRequest());

        // Validate the Checkpoints in the database
        List<Checkpoints> checkpointsList = checkpointsRepository.findAll();
        assertThat(checkpointsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkpointsRepository.findAll().size();
        // set the field null
        checkpoints.setLibelle(null);

        // Create the Checkpoints, which fails.


        restCheckpointsMockMvc.perform(post("/api/checkpoints").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoints)))
            .andExpect(status().isBadRequest());

        List<Checkpoints> checkpointsList = checkpointsRepository.findAll();
        assertThat(checkpointsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkpointsRepository.findAll().size();
        // set the field null
        checkpoints.setNote(null);

        // Create the Checkpoints, which fails.


        restCheckpointsMockMvc.perform(post("/api/checkpoints").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoints)))
            .andExpect(status().isBadRequest());

        List<Checkpoints> checkpointsList = checkpointsRepository.findAll();
        assertThat(checkpointsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCheckpoints() throws Exception {
        // Initialize the database
        checkpointsRepository.saveAndFlush(checkpoints);

        // Get all the checkpointsList
        restCheckpointsMockMvc.perform(get("/api/checkpoints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkpoints.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }
    
    @Test
    @Transactional
    public void getCheckpoints() throws Exception {
        // Initialize the database
        checkpointsRepository.saveAndFlush(checkpoints);

        // Get the checkpoints
        restCheckpointsMockMvc.perform(get("/api/checkpoints/{id}", checkpoints.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(checkpoints.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }
    @Test
    @Transactional
    public void getNonExistingCheckpoints() throws Exception {
        // Get the checkpoints
        restCheckpointsMockMvc.perform(get("/api/checkpoints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckpoints() throws Exception {
        // Initialize the database
        checkpointsRepository.saveAndFlush(checkpoints);

        int databaseSizeBeforeUpdate = checkpointsRepository.findAll().size();

        // Update the checkpoints
        Checkpoints updatedCheckpoints = checkpointsRepository.findById(checkpoints.getId()).get();
        // Disconnect from session so that the updates on updatedCheckpoints are not directly saved in db
        em.detach(updatedCheckpoints);
        updatedCheckpoints
            .libelle(UPDATED_LIBELLE)
            .note(UPDATED_NOTE);

        restCheckpointsMockMvc.perform(put("/api/checkpoints").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCheckpoints)))
            .andExpect(status().isOk());

        // Validate the Checkpoints in the database
        List<Checkpoints> checkpointsList = checkpointsRepository.findAll();
        assertThat(checkpointsList).hasSize(databaseSizeBeforeUpdate);
        Checkpoints testCheckpoints = checkpointsList.get(checkpointsList.size() - 1);
        assertThat(testCheckpoints.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCheckpoints.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingCheckpoints() throws Exception {
        int databaseSizeBeforeUpdate = checkpointsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckpointsMockMvc.perform(put("/api/checkpoints").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkpoints)))
            .andExpect(status().isBadRequest());

        // Validate the Checkpoints in the database
        List<Checkpoints> checkpointsList = checkpointsRepository.findAll();
        assertThat(checkpointsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCheckpoints() throws Exception {
        // Initialize the database
        checkpointsRepository.saveAndFlush(checkpoints);

        int databaseSizeBeforeDelete = checkpointsRepository.findAll().size();

        // Delete the checkpoints
        restCheckpointsMockMvc.perform(delete("/api/checkpoints/{id}", checkpoints.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Checkpoints> checkpointsList = checkpointsRepository.findAll();
        assertThat(checkpointsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
