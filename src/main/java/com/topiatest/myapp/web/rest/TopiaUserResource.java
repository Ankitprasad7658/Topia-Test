package com.topiatest.myapp.web.rest;

import com.topiatest.myapp.domain.TopiaUser;
import com.topiatest.myapp.repository.TopiaUserRepository;
import com.topiatest.myapp.service.TopiaUserQueryService;
import com.topiatest.myapp.service.TopiaUserService;
import com.topiatest.myapp.service.criteria.TopiaUserCriteria;
import com.topiatest.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.topiatest.myapp.domain.TopiaUser}.
 */
@RestController
@RequestMapping("/api")
public class TopiaUserResource {

    private final Logger log = LoggerFactory.getLogger(TopiaUserResource.class);

    private static final String ENTITY_NAME = "topiaUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopiaUserService topiaUserService;

    private final TopiaUserRepository topiaUserRepository;

    private final TopiaUserQueryService topiaUserQueryService;

    public TopiaUserResource(
        TopiaUserService topiaUserService,
        TopiaUserRepository topiaUserRepository,
        TopiaUserQueryService topiaUserQueryService
    ) {
        this.topiaUserService = topiaUserService;
        this.topiaUserRepository = topiaUserRepository;
        this.topiaUserQueryService = topiaUserQueryService;
    }

    /**
     * {@code POST  /topia-users} : Create a new topiaUser.
     *
     * @param topiaUser the topiaUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topiaUser, or with status {@code 400 (Bad Request)} if the topiaUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topia-users")
    public ResponseEntity<TopiaUser> createTopiaUser(@RequestBody TopiaUser topiaUser) throws URISyntaxException {
        log.debug("REST request to save TopiaUser : {}", topiaUser);
        if (topiaUser.getId() != null) {
            throw new BadRequestAlertException("A new topiaUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TopiaUser result = topiaUserService.save(topiaUser);
        return ResponseEntity
            .created(new URI("/api/topia-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topia-users/:id} : Updates an existing topiaUser.
     *
     * @param id the id of the topiaUser to save.
     * @param topiaUser the topiaUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topiaUser,
     * or with status {@code 400 (Bad Request)} if the topiaUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topiaUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topia-users/{id}")
    public ResponseEntity<TopiaUser> updateTopiaUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TopiaUser topiaUser
    ) throws URISyntaxException {
        log.debug("REST request to update TopiaUser : {}, {}", id, topiaUser);
        if (topiaUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topiaUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topiaUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TopiaUser result = topiaUserService.update(topiaUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topiaUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /topia-users/:id} : Partial updates given fields of an existing topiaUser, field will ignore if it is null
     *
     * @param id the id of the topiaUser to save.
     * @param topiaUser the topiaUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topiaUser,
     * or with status {@code 400 (Bad Request)} if the topiaUser is not valid,
     * or with status {@code 404 (Not Found)} if the topiaUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the topiaUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/topia-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TopiaUser> partialUpdateTopiaUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TopiaUser topiaUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update TopiaUser partially : {}, {}", id, topiaUser);
        if (topiaUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topiaUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topiaUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TopiaUser> result = topiaUserService.partialUpdate(topiaUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topiaUser.getId().toString())
        );
    }

    /**
     * {@code GET  /topia-users} : get all the topiaUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topiaUsers in body.
     */
    @GetMapping("/topia-users")
    public ResponseEntity<List<TopiaUser>> getAllTopiaUsers(
        TopiaUserCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TopiaUsers by criteria: {}", criteria);
        Page<TopiaUser> page = topiaUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /topia-users/count} : count all the topiaUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/topia-users/count")
    public ResponseEntity<Long> countTopiaUsers(TopiaUserCriteria criteria) {
        log.debug("REST request to count TopiaUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(topiaUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /topia-users/:id} : get the "id" topiaUser.
     *
     * @param id the id of the topiaUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topiaUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topia-users/{id}")
    public ResponseEntity<TopiaUser> getTopiaUser(@PathVariable Long id) {
        log.debug("REST request to get TopiaUser : {}", id);
        Optional<TopiaUser> topiaUser = topiaUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topiaUser);
    }

    /**
     * {@code DELETE  /topia-users/:id} : delete the "id" topiaUser.
     *
     * @param id the id of the topiaUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topia-users/{id}")
    public ResponseEntity<Void> deleteTopiaUser(@PathVariable Long id) {
        log.debug("REST request to delete TopiaUser : {}", id);
        topiaUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
