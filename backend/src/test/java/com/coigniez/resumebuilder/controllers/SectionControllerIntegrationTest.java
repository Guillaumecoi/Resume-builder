package com.coigniez.resumebuilder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionUpdateReq;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long resumeId;

    @BeforeEach
    @WithMockUser(username = "testuser", roles = "USER")
    void setUp() throws Exception {
        // Arrange
        ResumeCreateReq createRequest = ResumeCreateReq.builder().title("Software Developer").build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        resumeId = Long.parseLong(createResponse);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateSection() throws Exception {
        // Arrange
        SectionCreateReq request = SectionCreateReq.builder().resumeId(resumeId).title("Education").build();

        // Act
        String createResponse = mockMvc.perform(post("/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Assert
        mockMvc.perform(get("/sections/" + sectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Education"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateSectionNoAccess() throws Exception {
        // Arrange
        SectionCreateReq request = SectionCreateReq.builder().resumeId(resumeId).title("Education").build();

        String createResponse = mockMvc.perform(post("/sections")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Act & Assert
        mockMvc.perform(get("/sections/" + sectionId)
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/sections/" + sectionId)
                .with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Education"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateAndGetResumeWithSections() throws Exception {
        // Arrange
        SectionCreateReq section1 = SectionCreateReq.builder().resumeId(resumeId).title("Education").build();
        SectionCreateReq section2 = SectionCreateReq.builder().resumeId(resumeId).title("Experience").build();

        // Act
        ResumeCreateReq createRequest = ResumeCreateReq.builder()
                .title("Barista")
                .sections(List.of(section1, section2))
                .build();

        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long newResumeId = Long.parseLong(createResponse);

        // Assert
        mockMvc.perform(get("/resumes/" + newResumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sections[*].title")
                        .value(org.hamcrest.Matchers.containsInAnyOrder("Education", "Experience")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateSection() throws Exception {
        // Arrange
        SectionCreateReq request = SectionCreateReq.builder().resumeId(resumeId).title("Education").build();

        String createResponse = mockMvc.perform(post("/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Act
        SectionUpdateReq updateRequest = SectionUpdateReq.builder().id(sectionId).title("Experience")
                .showTitle(true).createSectionItems(List.of()).updateSectionItems(List.of()).build();

        mockMvc.perform(post("/sections/" + sectionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/sections/" + sectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Experience"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteSection() throws Exception {
        // Arrange
        SectionCreateReq request = SectionCreateReq.builder().resumeId(resumeId).title("Education").build();

        String createResponse = mockMvc.perform(post("/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Act
        mockMvc.perform(post("/sections/" + sectionId + "/delete"))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/sections/" + sectionId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateResumeDoesNotChangeSection() throws Exception {
        // Arrange
        SectionCreateReq request = SectionCreateReq.builder().resumeId(resumeId).title("Education").build();

        String createResponse = mockMvc.perform(post("/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Act
        ResumeCreateReq updateRequest = ResumeCreateReq.builder().title("Barista").build();

        mockMvc.perform(post("/resumes/" + resumeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/sections/" + sectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Education"));
    }
}
