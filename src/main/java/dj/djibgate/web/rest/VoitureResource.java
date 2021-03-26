package dj.djibgate.web.rest;

import dj.djibgate.domain.Voiture;
import dj.djibgate.repository.VoitureRepository;
import dj.djibgate.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link dj.djibgate.domain.Voiture}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoitureResource {

    private final Logger log = LoggerFactory.getLogger(VoitureResource.class);

    private static final String ENTITY_NAME = "djibGatesVoiture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoitureRepository voitureRepository;

    public VoitureResource(VoitureRepository voitureRepository) {
        this.voitureRepository = voitureRepository;
    }

    /**
     * {@code POST  /voitures} : Create a new voiture.
     *
     * @param voiture the voiture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voiture, or with status {@code 400 (Bad Request)} if the voiture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voitures")
    public ResponseEntity<Voiture> createVoiture(@Valid @RequestBody Voiture voiture) throws URISyntaxException {
        log.debug("REST request to save Voiture : {}", voiture);
        if (voiture.getId() != null) {
            throw new BadRequestAlertException("A new voiture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Voiture result = voitureRepository.save(voiture);
        return ResponseEntity.created(new URI("/api/voitures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /voitures} : Updates an existing voiture.
     *
     * @param voiture the voiture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voiture,
     * or with status {@code 400 (Bad Request)} if the voiture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voiture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voitures")
    public ResponseEntity<Voiture> updateVoiture(@Valid @RequestBody Voiture voiture) throws URISyntaxException {
        log.debug("REST request to update Voiture : {}", voiture);
        if (voiture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Voiture result = voitureRepository.save(voiture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voiture.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /voitures} : get all the voitures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voitures in body.
     */
    @GetMapping("/voitures")
    public ResponseEntity<List<Voiture>> getAllVoitures(Pageable pageable) {
        log.debug("REST request to get a page of Voitures");
        Page<Voiture> page = voitureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /voitures/:id} : get the "id" voiture.
     *
     * @param id the id of the voiture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voiture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voitures/{id}")
    public ResponseEntity<Voiture> getVoiture(@PathVariable Long id) {
        log.debug("REST request to get Voiture : {}", id);
        Optional<Voiture> voiture = voitureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voiture);
    }

    /**
     * {@code DELETE  /voitures/:id} : delete the "id" voiture.
     *
     * @param id the id of the voiture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voitures/{id}")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        log.debug("REST request to delete Voiture : {}", id);
        voitureRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
