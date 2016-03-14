package com.sgebs.web.rest;

import com.sgebs.Application;
import com.sgebs.domain.Invitation;
import com.sgebs.repository.InvitationRepository;
import com.sgebs.repository.search.InvitationSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InvitationResource REST controller.
 *
 * @see InvitationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InvitationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_ANSWER = false;
    private static final Boolean UPDATED_ANSWER = true;
    private static final String DEFAULT_MESSAGE = "AAAAA";
    private static final String UPDATED_MESSAGE = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CREATED_STR = dateTimeFormatter.format(DEFAULT_DATE_CREATED);

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.format(DEFAULT_LAST_UPDATED);

    @Inject
    private InvitationRepository invitationRepository;

    @Inject
    private InvitationSearchRepository invitationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInvitationMockMvc;

    private Invitation invitation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InvitationResource invitationResource = new InvitationResource();
        ReflectionTestUtils.setField(invitationResource, "invitationSearchRepository", invitationSearchRepository);
        ReflectionTestUtils.setField(invitationResource, "invitationRepository", invitationRepository);
        this.restInvitationMockMvc = MockMvcBuilders.standaloneSetup(invitationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        invitation = new Invitation();
        invitation.setAnswer(DEFAULT_ANSWER);
        invitation.setMessage(DEFAULT_MESSAGE);
        invitation.setDateCreated(DEFAULT_DATE_CREATED);
        invitation.setLastUpdated(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createInvitation() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation

        restInvitationMockMvc.perform(post("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeCreate + 1);
        Invitation testInvitation = invitations.get(invitations.size() - 1);
        assertThat(testInvitation.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testInvitation.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testInvitation.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testInvitation.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setDateCreated(null);

        // Create the Invitation, which fails.

        restInvitationMockMvc.perform(post("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isBadRequest());

        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setLastUpdated(null);

        // Create the Invitation, which fails.

        restInvitationMockMvc.perform(post("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isBadRequest());

        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvitations() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitations
        restInvitationMockMvc.perform(get("/api/invitations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
                .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.booleanValue())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED_STR)))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(invitation.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.booleanValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED_STR))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingInvitation() throws Exception {
        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

		int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation
        invitation.setAnswer(UPDATED_ANSWER);
        invitation.setMessage(UPDATED_MESSAGE);
        invitation.setDateCreated(UPDATED_DATE_CREATED);
        invitation.setLastUpdated(UPDATED_LAST_UPDATED);

        restInvitationMockMvc.perform(put("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitations.get(invitations.size() - 1);
        assertThat(testInvitation.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testInvitation.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testInvitation.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testInvitation.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void deleteInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

		int databaseSizeBeforeDelete = invitationRepository.findAll().size();

        // Get the invitation
        restInvitationMockMvc.perform(delete("/api/invitations/{id}", invitation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
