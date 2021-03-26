package dj.djibgate.web.rest;

import dj.djibgate.domain.Checkup;
import dj.djibgate.repository.CheckupRepository;
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
 * REST controller for managing {@link dj.djibgate.domain.Checkup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CheckupResource {

    private final Logger log = LoggerFactory.getLogger(CheckupResource.class);

    private static final String ENTITY_NAME = "djibGatesCheckup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CheckupRepository checkupRepository;

    public CheckupResource(CheckupRepository checkupRepository) {
        this.checkupRepository = checkupRepository;
    }

    /**
     * {@code POST  /checkups} : Create a new checkup.
     *
     * @param checkup the checkup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new checkup, or with status {@code 400 (Bad Request)} if the checkup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/checkups")
    public ResponseEntity<Checkup> createCheckup(@Valid @RequestBody Checkup checkup) throws URISyntaxException {
        log.debug("REST request to save Checkup : {}", checkup);
        if (checkup.getId() != null) {
            throw new BadRequestAlertException("A new checkup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Checkup result = checkupRepository.save(checkup);
        return ResponseEntity.created(new URI("/api/checkups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /checkups} : Updates an existing checkup.
     *
     * @param checkup the checkup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkup,
     * or with status {@code 400 (Bad Request)} if the checkup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the checkup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/checkups")
    public ResponseEntity<Checkup> updateCheckup(@Valid @RequestBody Checkup checkup) throws URISyntaxException {
        log.debug("REST request to update Checkup : {}", checkup);
        if (checkup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Checkup result = checkupRepository.save(checkup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, checkup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /checkups} : get all the checkups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of checkups in body.
     */
    @GetMapping("/checkups")
    public ResponseEntity<List<Checkup>> getAllCheckups(Pageable pageable) {
        log.debug("REST request to get a page of Checkups");
        Page<Checkup> page = checkupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /checkups/:id} : get the "id" checkup.
     *
     * @param id the id of the checkup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the checkup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/checkups/{id}")
    public ResponseEntity<Checkup> getCheckup(@PathVariable Long id) {
        log.debug("REST request to get Checkup : {}", id);
        Optional<Checkup> checkup = checkupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(checkup);
    }

    /**
     * {@code DELETE  /checkups/:id} : delete the "id" checkup.
     *
     * @param id the id of the checkup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/checkups/{id}")
    public ResponseEntity<Void> deleteCheckup(@PathVariable Long id) {
        log.debug("REST request to delete Checkup : {}", id);
        checkupRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
