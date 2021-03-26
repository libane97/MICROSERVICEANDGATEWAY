package dj.djibgate.web.rest;

import dj.djibgate.DjibGatesApp;
import dj.djibgate.config.TestSecurityConfiguration;
import dj.djibgate.domain.Voiture;
import dj.djibgate.repository.VoitureRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VoitureResource} REST controller.
 */
@SpringBootTest(classes = { DjibGatesApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class VoitureResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String DEFAULT_MOTEUR = "AAAAAAAAAA";
    private static final String UPDATED_MOTEUR = "BBBBBBBBBB";

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoitureMockMvc;

    private Voiture voiture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voiture createEntity(EntityManager em) {
        Voiture voiture = new Voiture()
            .title(DEFAULT_TITLE)
            .price(DEFAULT_PRICE)
            .moteur(DEFAULT_MOTEUR);
        return voiture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voiture createUpdatedEntity(EntityManager em) {
        Voiture voiture = new Voiture()
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .moteur(UPDATED_MOTEUR);
        return voiture;
    }

    @BeforeEach
    public void initTest() {
        voiture = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoiture() throws Exception {
        int databaseSizeBeforeCreate = voitureRepository.findAll().size();
        // Create the Voiture
        restVoitureMockMvc.perform(post("/api/voitures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isCreated());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeCreate + 1);
        Voiture testVoiture = voitureList.get(voitureList.size() - 1);
        assertThat(testVoiture.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVoiture.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVoiture.getMoteur()).isEqualTo(DEFAULT_MOTEUR);
    }

    @Test
    @Transactional
    public void createVoitureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voitureRepository.findAll().size();

        // Create the Voiture with an existing ID
        voiture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoitureMockMvc.perform(post("/api/voitures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isBadRequest());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = voitureRepository.findAll().size();
        // set the field null
        voiture.setTitle(null);

        // Create the Voiture, which fails.


        restVoitureMockMvc.perform(post("/api/voitures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isBadRequest());

        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = voitureRepository.findAll().size();
        // set the field null
        voiture.setPrice(null);

        // Create the Voiture, which fails.


        restVoitureMockMvc.perform(post("/api/voitures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isBadRequest());

        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMoteurIsRequired() throws Exception {
        int databaseSizeBeforeTest = voitureRepository.findAll().size();
        // set the field null
        voiture.setMoteur(null);

        // Create the Voiture, which fails.


        restVoitureMockMvc.perform(post("/api/voitures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isBadRequest());

        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVoitures() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        // Get all the voitureList
        restVoitureMockMvc.perform(get("/api/voitures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voiture.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].moteur").value(hasItem(DEFAULT_MOTEUR)));
    }
    
    @Test
    @Transactional
    public void getVoiture() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        // Get the voiture
        restVoitureMockMvc.perform(get("/api/voitures/{id}", voiture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voiture.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.moteur").value(DEFAULT_MOTEUR));
    }
    @Test
    @Transactional
    public void getNonExistingVoiture() throws Exception {
        // Get the voiture
        restVoitureMockMvc.perform(get("/api/voitures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoiture() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();

        // Update the voiture
        Voiture updatedVoiture = voitureRepository.findById(voiture.getId()).get();
        // Disconnect from session so that the updates on updatedVoiture are not directly saved in db
        em.detach(updatedVoiture);
        updatedVoiture
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .moteur(UPDATED_MOTEUR);

        restVoitureMockMvc.perform(put("/api/voitures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVoiture)))
            .andExpect(status().isOk());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
        Voiture testVoiture = voitureList.get(voitureList.size() - 1);
        assertThat(testVoiture.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVoiture.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVoiture.getMoteur()).isEqualTo(UPDATED_MOTEUR);
    }

    @Test
    @Transactional
    public void updateNonExistingVoiture() throws Exception {
        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoitureMockMvc.perform(put("/api/voitures").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isBadRequest());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVoiture() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        int databaseSizeBeforeDelete = voitureRepository.findAll().size();

        // Delete the voiture
        restVoitureMockMvc.perform(delete("/api/voitures/{id}", voiture.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
