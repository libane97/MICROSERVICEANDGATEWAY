package dj.djibgate.web.rest;

import dj.djibgate.domain.Checkpoints;
import dj.djibgate.repository.CheckpointsRepository;
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
 * REST controller for managing {@link dj.djibgate.domain.Checkpoints}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CheckpointsResource {

    private final Logger log = LoggerFactory.getLogger(CheckpointsResource.class);

    private static final String ENTITY_NAME = "djibGatesCheckpoints";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CheckpointsRepository checkpointsRepository;

    public CheckpointsResource(CheckpointsRepository checkpointsRepository) {
        this.checkpointsRepository = checkpointsRepository;
    }

    /**
     * {@code POST  /checkpoints} : Create a new checkpoints.
     *
     * @param checkpoints the checkpoints to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new checkpoints, or with status {@code 400 (Bad Request)} if the checkpoints has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/checkpoints")
    public ResponseEntity<Checkpoints> createCheckpoints(@Valid @RequestBody Checkpoints checkpoints) throws URISyntaxException {
        log.debug("REST request to save Checkpoints : {}", checkpoints);
        if (checkpoints.getId() != null) {
            throw new BadRequestAlertException("A new checkpoints cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Checkpoints result = checkpointsRepository.save(checkpoints);
        return ResponseEntity.created(new URI("/api/checkpoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /checkpoints} : Updates an existing checkpoints.
     *
     * @param checkpoints the checkpoints to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkpoints,
     * or with status {@code 400 (Bad Request)} if the checkpoints is not valid,
     * or with status {@code 500 (Internal Server Error)} if the checkpoints couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/checkpoints")
    public ResponseEntity<Checkpoints> updateCheckpoints(@Valid @RequestBody Checkpoints checkpoints) throws URISyntaxException {
        log.debug("REST request to update Checkpoints : {}", checkpoints);
        if (checkpoints.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Checkpoints result = checkpointsRepository.save(checkpoints);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, checkpoints.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /checkpoints} : get all the checkpoints.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of checkpoints in body.
     */
    @GetMapping("/checkpoints")
    public ResponseEntity<List<Checkpoints>> getAllCheckpoints(Pageable pageable) {
        log.debug("REST request to get a page of Checkpoints");
        Page<Checkpoints> page = checkpointsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /checkpoints/:id} : get the "id" checkpoints.
     *
     * @param id the id of the checkpoints to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the checkpoints, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/checkpoints/{id}")
    public ResponseEntity<Checkpoints> getCheckpoints(@PathVariable Long id) {
        log.debug("REST request to get Checkpoints : {}", id);
        Optional<Checkpoints> checkpoints = checkpointsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(checkpoints);
    }

    /**
     * {@code DELETE  /checkpoints/:id} : delete the "id" checkpoints.
     *
     * @param id the id of the checkpoints to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/checkpoints/{id}")
    public ResponseEntity<Void> deleteCheckpoints(@PathVariable Long id) {
        log.debug("REST request to delete Checkpoints : {}", id);
        checkpointsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
