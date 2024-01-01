package com.topiatest.myapp.repository;

import com.topiatest.myapp.domain.TopiaUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TopiaUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopiaUserRepository extends JpaRepository<TopiaUser, Long>, JpaSpecificationExecutor<TopiaUser> {}
