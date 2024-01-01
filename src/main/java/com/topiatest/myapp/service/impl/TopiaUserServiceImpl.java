package com.topiatest.myapp.service.impl;

import com.topiatest.myapp.domain.TopiaUser;
import com.topiatest.myapp.repository.TopiaUserRepository;
import com.topiatest.myapp.service.TopiaUserService;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TopiaUser}.
 */
@Service
@Transactional
public class TopiaUserServiceImpl implements TopiaUserService {

    private final Logger log = LoggerFactory.getLogger(TopiaUserServiceImpl.class);

    private final TopiaUserRepository topiaUserRepository;

    public TopiaUserServiceImpl(TopiaUserRepository topiaUserRepository) {
        this.topiaUserRepository = topiaUserRepository;
    }

    @Override
    public TopiaUser save(TopiaUser topiaUser) {
        log.debug("Request to save TopiaUser : {}", topiaUser);
        topiaUser.setCreatedDate(LocalDate.now());
        topiaUser.setCreatedBy(1L);
        topiaUser.setUpdatedDate(LocalDate.now());
        topiaUser.setUpdatedBy(1L);
        topiaUser.setStatus("Active");
        
        String firstName = topiaUser.getFirstName();
        String lastName = topiaUser.getLastName();
        
        topiaUser.setName(firstName +" "+ lastName);
        return topiaUserRepository.save(topiaUser);
    }

    @Override
    public TopiaUser update(TopiaUser topiaUser) {
        log.debug("Request to save TopiaUser : {}", topiaUser);
        topiaUser.setCreatedDate(LocalDate.now());
        topiaUser.setCreatedBy(1L);
        topiaUser.setUpdatedDate(LocalDate.now());
        topiaUser.setUpdatedBy(1L);
        topiaUser.setStatus("Active");
        
        String firstName = topiaUser.getFirstName();
        String lastName = topiaUser.getLastName();
        
        topiaUser.setName(firstName +" "+ lastName);
        return topiaUserRepository.save(topiaUser);
    }

    @Override
    public Optional<TopiaUser> partialUpdate(TopiaUser topiaUser) {
        log.debug("Request to partially update TopiaUser : {}", topiaUser);

        return topiaUserRepository
            .findById(topiaUser.getId())
            .map(existingTopiaUser -> {
                if (topiaUser.getName() != null) {
                    existingTopiaUser.setName(topiaUser.getName());
                }
                if (topiaUser.getDateOfBirth() != null) {
                    existingTopiaUser.setDateOfBirth(topiaUser.getDateOfBirth());
                }
                if (topiaUser.getUserName() != null) {
                    existingTopiaUser.setUserName(topiaUser.getUserName());
                }
                if (topiaUser.getEmail() != null) {
                    existingTopiaUser.setEmail(topiaUser.getEmail());
                }
                if (topiaUser.getCreatedDate() != null) {
                    existingTopiaUser.setCreatedDate(topiaUser.getCreatedDate());
                }
                if (topiaUser.getCreatedBy() != null) {
                    existingTopiaUser.setCreatedBy(topiaUser.getCreatedBy());
                }
                if (topiaUser.getUpdatedDate() != null) {
                    existingTopiaUser.setUpdatedDate(topiaUser.getUpdatedDate());
                }
                if (topiaUser.getUpdatedBy() != null) {
                    existingTopiaUser.setUpdatedBy(topiaUser.getUpdatedBy());
                }
                if (topiaUser.getStatus() != null) {
                    existingTopiaUser.setStatus(topiaUser.getStatus());
                }

                return existingTopiaUser;
            })
            .map(topiaUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopiaUser> findAll(Pageable pageable) {
        log.debug("Request to get all TopiaUsers");
        return topiaUserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopiaUser> findOne(Long id) {
        log.debug("Request to get TopiaUser : {}", id);
        return topiaUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TopiaUser : {}", id);
        topiaUserRepository.deleteById(id);
    }
}
