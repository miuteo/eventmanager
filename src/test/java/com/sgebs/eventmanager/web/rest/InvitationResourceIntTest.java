package com.sgebs.eventmanager.web.rest;

import com.sgebs.eventmanager.EventmanagerApp;
import com.sgebs.eventmanager.domain.Invitation;
import com.sgebs.eventmanager.repository.InvitationRepository;
import com.sgebs.eventmanager.service.InvitationService;

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
@SpringApplicationConfiguration(classes = EventmanagerApp.class)
@WebAppConfiguration
@IntegrationTest
public class InvitationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final Boolean DEFAULT_ACCEPT = false;
    private static final Boolean UPDATED_ACCEPT = true;

    @Inject
    private InvitationRepository invitationRepository;

    @Inject
    private InvitationService invitationService;

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
        ReflectionTestUtils.setField(invitationResource, "invitationService", invitationService);
        this.restInvitationMockMvc = MockMvcBuilders.standaloneSetup(invitationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        invitation = new Invitation();
        invitation.setDate(DEFAULT_DATE);
        invitation.setAccept(DEFAULT_ACCEPT);
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
        assertThat(testInvitation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInvitation.isAccept()).isEqualTo(DEFAULT_ACCEPT);
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
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].accept").value(hasItem(DEFAULT_ACCEPT.booleanValue())));
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
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.accept").value(DEFAULT_ACCEPT.booleanValue()));
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
        invitationService.save(invitation);

        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation
        Invitation updatedInvitation = new Invitation();
        updatedInvitation.setId(invitation.getId());
        updatedInvitation.setDate(UPDATED_DATE);
        updatedInvitation.setAccept(UPDATED_ACCEPT);

        restInvitationMockMvc.perform(put("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInvitation)))
                .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitations.get(invitations.size() - 1);
        assertThat(testInvitation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInvitation.isAccept()).isEqualTo(UPDATED_ACCEPT);
    }

    @Test
    @Transactional
    public void deleteInvitation() throws Exception {
        // Initialize the database
        invitationService.save(invitation);

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
