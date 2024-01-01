package com.topiatest.myapp.service;

import com.topiatest.myapp.domain.TopiaUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TopiaUser}.
 */
public interface TopiaUserService {
    /**
     * Save a topiaUser.
     *
     * @param topiaUser the entity to save.
     * @return the persisted entity.
     */
    TopiaUser save(TopiaUser topiaUser);

    /**
     * Updates a topiaUser.
     *
     * @param topiaUser the entity to update.
     * @return the persisted entity.
     */
    TopiaUser update(TopiaUser topiaUser);

    /**
     * Partially updates a topiaUser.
     *
     * @param topiaUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TopiaUser> partialUpdate(TopiaUser topiaUser);

    /**
     * Get all the topiaUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopiaUser> findAll(Pageable pageable);

    /**
     * Get the "id" topiaUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopiaUser> findOne(Long id);

    /**
     * Delete the "id" topiaUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
