package com.coigniez.resumebuilder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionSimpleCreateReq;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ResumeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testCreateAndGetResume() throws Exception {
        // Arrange
        ResumeCreateReq createRequest = ResumeCreateReq.builder().title("Software Engineer")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        // Assert - Get created resume
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"))
                .andExpect(jsonPath("$.sections[*].title")
                        .value(org.hamcrest.Matchers.containsInAnyOrder("Education", "Experience")));
    }

    @Test
    void testResumeAccessControl() throws Exception {
        // Create resume as testuser
        ResumeCreateReq createRequest = ResumeCreateReq.builder().title("Software Engineer").build();

        String createResponse = mockMvc.perform(post("/resumes")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        // Try access with different user - should fail
        mockMvc.perform(get("/resumes/" + resumeId)
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound()); // Not forbidden, but not found for security reasons

        // Original user can still access
        mockMvc.perform(get("/resumes/" + resumeId)
                .with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testUpdate() throws Exception {
        // Arrange
        ResumeCreateReq createRequest = ResumeCreateReq.builder().title("Software Engineer").build();
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        ResumeCreateReq updateRequest = ResumeCreateReq.builder().title("Barista").build();

        // Act
        mockMvc.perform(post("/resumes/" + resumeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Barista"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testDelete() throws Exception {
        // Arrange
        ResumeCreateReq createRequest = ResumeCreateReq.builder().title("Software Engineer").build();
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        // Act
        mockMvc.perform(post("/resumes/" + resumeId + "/delete"))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testGetAllAndDeleteAll() throws Exception {
        // Arrange
        ResumeCreateReq createRequest1 = ResumeCreateReq.builder().title("Software Engineer").build();
        mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest1)))
                .andExpect(status().isCreated());

        ResumeCreateReq createRequest2 = ResumeCreateReq.builder().title("Barista").build();
        mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest2)))
                .andExpect(status().isCreated());

        // Act - Get all
        mockMvc.perform(get("/resumes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[*].title")
                        .value(org.hamcrest.Matchers.containsInAnyOrder("Software Engineer", "Barista")))
                .andExpect(jsonPath("$.totalElements").value(2));

        // Act - Delete all
        mockMvc.perform(post("/resumes/deleteAll"))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/resumes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetChildren() throws Exception {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Test Resume")
                .build();

        String resumeResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(resumeRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long resumeId = Long.parseLong(resumeResponse);

        // Create sections
        SectionCreateReq sectionRequest = SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();
        mockMvc.perform(post("/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sectionRequest)))
                .andExpect(status().isCreated());

        // Create layout
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .build();
        mockMvc.perform(post("/layouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(layoutRequest)))
                .andExpect(status().isCreated());

        // Act & Assert - Get sections
        mockMvc.perform(get("/resumes/" + resumeId + "/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Section"));

        // Get layouts
        mockMvc.perform(get("/resumes/" + resumeId + "/layouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].pageSize").value("A4"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteChildren() throws Exception {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Test Resume")
                .build();

        String resumeResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(resumeRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long resumeId = Long.parseLong(resumeResponse);

        // Create sections
        SectionCreateReq sectionRequest = SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();
        mockMvc.perform(post("/sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sectionRequest)))
                .andExpect(status().isCreated());

        // Create layout
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .build();
        mockMvc.perform(post("/layouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(layoutRequest)))
                .andExpect(status().isCreated());

        // Act & Assert - Delete sections
        mockMvc.perform(post("/resumes/" + resumeId + "/delete/sections"))
                .andExpect(status().isNoContent());

        // Verify sections are deleted
        mockMvc.perform(get("/resumes/" + resumeId + "/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testChildrenAccessControl() throws Exception {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Test Resume")
                .build();

        String resumeResponse = mockMvc.perform(post("/resumes")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(resumeRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long resumeId = Long.parseLong(resumeResponse);

        // Create sections
        SectionCreateReq sectionRequest = SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();
        mockMvc.perform(post("/sections")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sectionRequest)))
                .andExpect(status().isCreated());

        // Create layout
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .build();
        mockMvc.perform(post("/layouts")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(layoutRequest)))
                .andExpect(status().isCreated());

        // Try access with different user - should fail
        mockMvc.perform(get("/resumes/" + resumeId + "/sections")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound()); 
        mockMvc.perform(get("/resumes/" + resumeId + "/layouts")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/resumes/" + resumeId + "/delete/sections")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/resumes/" + resumeId + "/delete/layouts")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());

    }

}