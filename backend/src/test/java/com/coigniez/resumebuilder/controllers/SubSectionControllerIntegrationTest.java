package com.coigniez.resumebuilder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionUpdateReq;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SubSectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long sectionId;

    @BeforeEach
    void setUp() throws Exception {
        // Create a resume first
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Developer")
                .build();

        String resumeResponse = mockMvc.perform(post("/resumes")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(resumeRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(resumeResponse);

        // Create a section
        SectionCreateReq sectionRequest = SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Education")
                .build();

        String sectionResponse = mockMvc.perform(post("/sections")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sectionRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        sectionId = Long.parseLong(sectionResponse);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateAndGetSubSection() throws Exception {
        // Arrange
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Bachelor's Degree")
                .icon("school")
                .showTitle(true)
                .build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/subsections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long subSectionId = Long.parseLong(createResponse);

        // Assert - Get created subsection
        mockMvc.perform(get("/subsections/" + subSectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bachelor's Degree"))
                .andExpect(jsonPath("$.icon").value("school"))
                .andExpect(jsonPath("$.showTitle").value(true))
                .andExpect(jsonPath("$.sectionItems").isEmpty());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateSubSection() throws Exception {
        // Arrange
        SubSectionCreateReq createRequest = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Bachelor's Degree")
                .icon("school")
                .build();

        String createResponse = mockMvc.perform(post("/subsections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long subSectionId = Long.parseLong(createResponse);

        // Act - Update
        SubSectionUpdateReq updateRequest = SubSectionUpdateReq.builder()
                .id(subSectionId)
                .sectionId(sectionId)
                .title("Master's Degree")
                .icon("graduation-cap")
                .showTitle(false)
                .build();

        mockMvc.perform(post("/subsections/" + subSectionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/subsections/" + subSectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Master's Degree"))
                .andExpect(jsonPath("$.icon").value("graduation-cap"))
                .andExpect(jsonPath("$.showTitle").value(false));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteSubSection() throws Exception {
        // Arrange
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Bachelor's Degree")
                .build();

        String createResponse = mockMvc.perform(post("/subsections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long subSectionId = Long.parseLong(createResponse);

        // Act
        mockMvc.perform(post("/subsections/" + subSectionId + "/delete"))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/subsections/" + subSectionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSubSectionAccessControl() throws Exception {
        // Arrange - Create subsection as testuser
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Bachelor's Degree")
                .build();

        String createResponse = mockMvc.perform(post("/subsections")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long subSectionId = Long.parseLong(createResponse);

        // Assert - Try to access with other user
        // Create
        mockMvc.perform(post("/subsections")
                .with(user("otheruser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
        // Get
        mockMvc.perform(get("/subsections/" + subSectionId)
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());

        // Update
        mockMvc.perform(post("/subsections/" + subSectionId)
                .with(user("otheruser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        SubSectionUpdateReq.builder()
                                .id(subSectionId)
                                .sectionId(sectionId)
                                .title("Updated")
                                .showTitle(true)
                                .build())))
                .andExpect(status().isNotFound());

        // Delete
        mockMvc.perform(post("/subsections/" + subSectionId + "/delete")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());
    }
}