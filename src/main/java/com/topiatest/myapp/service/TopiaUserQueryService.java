package com.topiatest.myapp.service;

import com.topiatest.myapp.domain.*; // for static metamodels
import com.topiatest.myapp.domain.TopiaUser;
import com.topiatest.myapp.repository.TopiaUserRepository;
import com.topiatest.myapp.service.criteria.TopiaUserCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TopiaUser} entities in the database.
 * The main input is a {@link TopiaUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TopiaUser} or a {@link Page} of {@link TopiaUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TopiaUserQueryService extends QueryService<TopiaUser> {

    private final Logger log = LoggerFactory.getLogger(TopiaUserQueryService.class);

    private final TopiaUserRepository topiaUserRepository;

    public TopiaUserQueryService(TopiaUserRepository topiaUserRepository) {
        this.topiaUserRepository = topiaUserRepository;
    }

    /**
     * Return a {@link List} of {@link TopiaUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TopiaUser> findByCriteria(TopiaUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TopiaUser> specification = createSpecification(criteria);
        return topiaUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TopiaUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TopiaUser> findByCriteria(TopiaUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TopiaUser> specification = createSpecification(criteria);
        return topiaUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TopiaUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TopiaUser> specification = createSpecification(criteria);
        return topiaUserRepository.count(specification);
    }

    /**
     * Function to convert {@link TopiaUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TopiaUser> createSpecification(TopiaUserCriteria criteria) {
        Specification<TopiaUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TopiaUser_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TopiaUser_.name));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), TopiaUser_.dateOfBirth));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), TopiaUser_.userName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), TopiaUser_.email));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TopiaUser_.createdDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TopiaUser_.createdBy));
            }
            if (criteria.getUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedDate(), TopiaUser_.updatedDate));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), TopiaUser_.updatedBy));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), TopiaUser_.status));
            }
        }
        return specification;
    }
}
