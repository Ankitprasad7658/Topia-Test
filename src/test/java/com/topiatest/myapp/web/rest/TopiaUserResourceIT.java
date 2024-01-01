package com.topiatest.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.topiatest.myapp.IntegrationTest;
import com.topiatest.myapp.domain.TopiaUser;
import com.topiatest.myapp.repository.TopiaUserRepository;
import com.topiatest.myapp.service.criteria.TopiaUserCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TopiaUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TopiaUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;
    private static final Long SMALLER_CREATED_BY = 1L - 1L;

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_UPDATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_UPDATED_BY = 1L;
    private static final Long UPDATED_UPDATED_BY = 2L;
    private static final Long SMALLER_UPDATED_BY = 1L - 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/topia-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TopiaUserRepository topiaUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopiaUserMockMvc;

    private TopiaUser topiaUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopiaUser createEntity(EntityManager em) {
        TopiaUser topiaUser = new TopiaUser()
            .name(DEFAULT_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .userName(DEFAULT_USER_NAME)
            .email(DEFAULT_EMAIL)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .updatedBy(DEFAULT_UPDATED_BY)
            .status(DEFAULT_STATUS);
        return topiaUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopiaUser createUpdatedEntity(EntityManager em) {
        TopiaUser topiaUser = new TopiaUser()
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS);
        return topiaUser;
    }

    @BeforeEach
    public void initTest() {
        topiaUser = createEntity(em);
    }

    @Test
    @Transactional
    void createTopiaUser() throws Exception {
        int databaseSizeBeforeCreate = topiaUserRepository.findAll().size();
        // Create the TopiaUser
        restTopiaUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topiaUser)))
            .andExpect(status().isCreated());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeCreate + 1);
        TopiaUser testTopiaUser = topiaUserList.get(topiaUserList.size() - 1);
        assertThat(testTopiaUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTopiaUser.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testTopiaUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testTopiaUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTopiaUser.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTopiaUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTopiaUser.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testTopiaUser.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTopiaUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createTopiaUserWithExistingId() throws Exception {
        // Create the TopiaUser with an existing ID
        topiaUser.setId(1L);

        int databaseSizeBeforeCreate = topiaUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopiaUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topiaUser)))
            .andExpect(status().isBadRequest());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTopiaUsers() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList
        restTopiaUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topiaUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getTopiaUser() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get the topiaUser
        restTopiaUserMockMvc
            .perform(get(ENTITY_API_URL_ID, topiaUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topiaUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getTopiaUsersByIdFiltering() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        Long id = topiaUser.getId();

        defaultTopiaUserShouldBeFound("id.equals=" + id);
        defaultTopiaUserShouldNotBeFound("id.notEquals=" + id);

        defaultTopiaUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTopiaUserShouldNotBeFound("id.greaterThan=" + id);

        defaultTopiaUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTopiaUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where name equals to DEFAULT_NAME
        defaultTopiaUserShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the topiaUserList where name equals to UPDATED_NAME
        defaultTopiaUserShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where name not equals to DEFAULT_NAME
        defaultTopiaUserShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the topiaUserList where name not equals to UPDATED_NAME
        defaultTopiaUserShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTopiaUserShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the topiaUserList where name equals to UPDATED_NAME
        defaultTopiaUserShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where name is not null
        defaultTopiaUserShouldBeFound("name.specified=true");

        // Get all the topiaUserList where name is null
        defaultTopiaUserShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByNameContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where name contains DEFAULT_NAME
        defaultTopiaUserShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the topiaUserList where name contains UPDATED_NAME
        defaultTopiaUserShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where name does not contain DEFAULT_NAME
        defaultTopiaUserShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the topiaUserList where name does not contain UPDATED_NAME
        defaultTopiaUserShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultTopiaUserShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the topiaUserList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultTopiaUserShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultTopiaUserShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the topiaUserList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultTopiaUserShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultTopiaUserShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the topiaUserList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultTopiaUserShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth is not null
        defaultTopiaUserShouldBeFound("dateOfBirth.specified=true");

        // Get all the topiaUserList where dateOfBirth is null
        defaultTopiaUserShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultTopiaUserShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the topiaUserList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultTopiaUserShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultTopiaUserShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the topiaUserList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultTopiaUserShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultTopiaUserShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the topiaUserList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultTopiaUserShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultTopiaUserShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the topiaUserList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultTopiaUserShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where userName equals to DEFAULT_USER_NAME
        defaultTopiaUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the topiaUserList where userName equals to UPDATED_USER_NAME
        defaultTopiaUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where userName not equals to DEFAULT_USER_NAME
        defaultTopiaUserShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the topiaUserList where userName not equals to UPDATED_USER_NAME
        defaultTopiaUserShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultTopiaUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the topiaUserList where userName equals to UPDATED_USER_NAME
        defaultTopiaUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where userName is not null
        defaultTopiaUserShouldBeFound("userName.specified=true");

        // Get all the topiaUserList where userName is null
        defaultTopiaUserShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where userName contains DEFAULT_USER_NAME
        defaultTopiaUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the topiaUserList where userName contains UPDATED_USER_NAME
        defaultTopiaUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where userName does not contain DEFAULT_USER_NAME
        defaultTopiaUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the topiaUserList where userName does not contain UPDATED_USER_NAME
        defaultTopiaUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where email equals to DEFAULT_EMAIL
        defaultTopiaUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the topiaUserList where email equals to UPDATED_EMAIL
        defaultTopiaUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where email not equals to DEFAULT_EMAIL
        defaultTopiaUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the topiaUserList where email not equals to UPDATED_EMAIL
        defaultTopiaUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTopiaUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the topiaUserList where email equals to UPDATED_EMAIL
        defaultTopiaUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where email is not null
        defaultTopiaUserShouldBeFound("email.specified=true");

        // Get all the topiaUserList where email is null
        defaultTopiaUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where email contains DEFAULT_EMAIL
        defaultTopiaUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the topiaUserList where email contains UPDATED_EMAIL
        defaultTopiaUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where email does not contain DEFAULT_EMAIL
        defaultTopiaUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the topiaUserList where email does not contain UPDATED_EMAIL
        defaultTopiaUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate equals to DEFAULT_CREATED_DATE
        defaultTopiaUserShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the topiaUserList where createdDate equals to UPDATED_CREATED_DATE
        defaultTopiaUserShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultTopiaUserShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the topiaUserList where createdDate not equals to UPDATED_CREATED_DATE
        defaultTopiaUserShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultTopiaUserShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the topiaUserList where createdDate equals to UPDATED_CREATED_DATE
        defaultTopiaUserShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate is not null
        defaultTopiaUserShouldBeFound("createdDate.specified=true");

        // Get all the topiaUserList where createdDate is null
        defaultTopiaUserShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultTopiaUserShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the topiaUserList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultTopiaUserShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultTopiaUserShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the topiaUserList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultTopiaUserShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate is less than DEFAULT_CREATED_DATE
        defaultTopiaUserShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the topiaUserList where createdDate is less than UPDATED_CREATED_DATE
        defaultTopiaUserShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultTopiaUserShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the topiaUserList where createdDate is greater than SMALLER_CREATED_DATE
        defaultTopiaUserShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy equals to DEFAULT_CREATED_BY
        defaultTopiaUserShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the topiaUserList where createdBy equals to UPDATED_CREATED_BY
        defaultTopiaUserShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy not equals to DEFAULT_CREATED_BY
        defaultTopiaUserShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the topiaUserList where createdBy not equals to UPDATED_CREATED_BY
        defaultTopiaUserShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTopiaUserShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the topiaUserList where createdBy equals to UPDATED_CREATED_BY
        defaultTopiaUserShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy is not null
        defaultTopiaUserShouldBeFound("createdBy.specified=true");

        // Get all the topiaUserList where createdBy is null
        defaultTopiaUserShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTopiaUserShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the topiaUserList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTopiaUserShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTopiaUserShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the topiaUserList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTopiaUserShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy is less than DEFAULT_CREATED_BY
        defaultTopiaUserShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the topiaUserList where createdBy is less than UPDATED_CREATED_BY
        defaultTopiaUserShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTopiaUserShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the topiaUserList where createdBy is greater than SMALLER_CREATED_BY
        defaultTopiaUserShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultTopiaUserShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the topiaUserList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultTopiaUserShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultTopiaUserShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the topiaUserList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultTopiaUserShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultTopiaUserShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the topiaUserList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultTopiaUserShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate is not null
        defaultTopiaUserShouldBeFound("updatedDate.specified=true");

        // Get all the topiaUserList where updatedDate is null
        defaultTopiaUserShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate is greater than or equal to DEFAULT_UPDATED_DATE
        defaultTopiaUserShouldBeFound("updatedDate.greaterThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the topiaUserList where updatedDate is greater than or equal to UPDATED_UPDATED_DATE
        defaultTopiaUserShouldNotBeFound("updatedDate.greaterThanOrEqual=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate is less than or equal to DEFAULT_UPDATED_DATE
        defaultTopiaUserShouldBeFound("updatedDate.lessThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the topiaUserList where updatedDate is less than or equal to SMALLER_UPDATED_DATE
        defaultTopiaUserShouldNotBeFound("updatedDate.lessThanOrEqual=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate is less than DEFAULT_UPDATED_DATE
        defaultTopiaUserShouldNotBeFound("updatedDate.lessThan=" + DEFAULT_UPDATED_DATE);

        // Get all the topiaUserList where updatedDate is less than UPDATED_UPDATED_DATE
        defaultTopiaUserShouldBeFound("updatedDate.lessThan=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedDate is greater than DEFAULT_UPDATED_DATE
        defaultTopiaUserShouldNotBeFound("updatedDate.greaterThan=" + DEFAULT_UPDATED_DATE);

        // Get all the topiaUserList where updatedDate is greater than SMALLER_UPDATED_DATE
        defaultTopiaUserShouldBeFound("updatedDate.greaterThan=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultTopiaUserShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the topiaUserList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTopiaUserShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy not equals to DEFAULT_UPDATED_BY
        defaultTopiaUserShouldNotBeFound("updatedBy.notEquals=" + DEFAULT_UPDATED_BY);

        // Get all the topiaUserList where updatedBy not equals to UPDATED_UPDATED_BY
        defaultTopiaUserShouldBeFound("updatedBy.notEquals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultTopiaUserShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the topiaUserList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTopiaUserShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy is not null
        defaultTopiaUserShouldBeFound("updatedBy.specified=true");

        // Get all the topiaUserList where updatedBy is null
        defaultTopiaUserShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultTopiaUserShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the topiaUserList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultTopiaUserShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultTopiaUserShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the topiaUserList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultTopiaUserShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultTopiaUserShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the topiaUserList where updatedBy is less than UPDATED_UPDATED_BY
        defaultTopiaUserShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultTopiaUserShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the topiaUserList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultTopiaUserShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where status equals to DEFAULT_STATUS
        defaultTopiaUserShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the topiaUserList where status equals to UPDATED_STATUS
        defaultTopiaUserShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where status not equals to DEFAULT_STATUS
        defaultTopiaUserShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the topiaUserList where status not equals to UPDATED_STATUS
        defaultTopiaUserShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTopiaUserShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the topiaUserList where status equals to UPDATED_STATUS
        defaultTopiaUserShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where status is not null
        defaultTopiaUserShouldBeFound("status.specified=true");

        // Get all the topiaUserList where status is null
        defaultTopiaUserShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTopiaUsersByStatusContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where status contains DEFAULT_STATUS
        defaultTopiaUserShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the topiaUserList where status contains UPDATED_STATUS
        defaultTopiaUserShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTopiaUsersByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        // Get all the topiaUserList where status does not contain DEFAULT_STATUS
        defaultTopiaUserShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the topiaUserList where status does not contain UPDATED_STATUS
        defaultTopiaUserShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTopiaUserShouldBeFound(String filter) throws Exception {
        restTopiaUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topiaUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restTopiaUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTopiaUserShouldNotBeFound(String filter) throws Exception {
        restTopiaUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTopiaUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTopiaUser() throws Exception {
        // Get the topiaUser
        restTopiaUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTopiaUser() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();

        // Update the topiaUser
        TopiaUser updatedTopiaUser = topiaUserRepository.findById(topiaUser.getId()).get();
        // Disconnect from session so that the updates on updatedTopiaUser are not directly saved in db
        em.detach(updatedTopiaUser);
        updatedTopiaUser
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS);

        restTopiaUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTopiaUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTopiaUser))
            )
            .andExpect(status().isOk());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
        TopiaUser testTopiaUser = topiaUserList.get(topiaUserList.size() - 1);
        assertThat(testTopiaUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTopiaUser.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testTopiaUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testTopiaUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTopiaUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTopiaUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTopiaUser.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testTopiaUser.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTopiaUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingTopiaUser() throws Exception {
        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();
        topiaUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopiaUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, topiaUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topiaUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTopiaUser() throws Exception {
        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();
        topiaUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopiaUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topiaUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTopiaUser() throws Exception {
        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();
        topiaUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopiaUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topiaUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTopiaUserWithPatch() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();

        // Update the topiaUser using partial update
        TopiaUser partialUpdatedTopiaUser = new TopiaUser();
        partialUpdatedTopiaUser.setId(topiaUser.getId());

        partialUpdatedTopiaUser.dateOfBirth(UPDATED_DATE_OF_BIRTH).createdDate(UPDATED_CREATED_DATE).updatedDate(UPDATED_UPDATED_DATE);

        restTopiaUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopiaUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopiaUser))
            )
            .andExpect(status().isOk());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
        TopiaUser testTopiaUser = topiaUserList.get(topiaUserList.size() - 1);
        assertThat(testTopiaUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTopiaUser.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testTopiaUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testTopiaUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTopiaUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTopiaUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTopiaUser.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testTopiaUser.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTopiaUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateTopiaUserWithPatch() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();

        // Update the topiaUser using partial update
        TopiaUser partialUpdatedTopiaUser = new TopiaUser();
        partialUpdatedTopiaUser.setId(topiaUser.getId());

        partialUpdatedTopiaUser
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS);

        restTopiaUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopiaUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopiaUser))
            )
            .andExpect(status().isOk());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
        TopiaUser testTopiaUser = topiaUserList.get(topiaUserList.size() - 1);
        assertThat(testTopiaUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTopiaUser.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testTopiaUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testTopiaUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTopiaUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTopiaUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTopiaUser.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testTopiaUser.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTopiaUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingTopiaUser() throws Exception {
        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();
        topiaUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopiaUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, topiaUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topiaUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTopiaUser() throws Exception {
        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();
        topiaUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopiaUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topiaUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTopiaUser() throws Exception {
        int databaseSizeBeforeUpdate = topiaUserRepository.findAll().size();
        topiaUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopiaUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(topiaUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TopiaUser in the database
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTopiaUser() throws Exception {
        // Initialize the database
        topiaUserRepository.saveAndFlush(topiaUser);

        int databaseSizeBeforeDelete = topiaUserRepository.findAll().size();

        // Delete the topiaUser
        restTopiaUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, topiaUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TopiaUser> topiaUserList = topiaUserRepository.findAll();
        assertThat(topiaUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
