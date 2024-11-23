package com.coigniez.resumebuilder.model.section;

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

import com.coigniez.resumebuilder.model.resume.ResumeRequest;
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
        ResumeRequest createRequest = new ResumeRequest("Software Engineer", "John", "Doe");
    
        // Act - Create
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        resumeId = Long.parseLong(createResponse);
    }

    @Test
    void testCreateSection() throws Exception {
        // Arrange
        SectionRequest request = new SectionRequest("Education");

        // Act
        String createResponse = mockMvc.perform(post("/resumes/" + resumeId + "/sections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId + "/sections/" + sectionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Education"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateSectionNoAccess() throws Exception {
        // Arrange
        SectionRequest request = new SectionRequest("Education");

        String createResponse = mockMvc.perform(post("/resumes/" + resumeId + "/sections")
            .with(user("testuser").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Act & Assert
        mockMvc.perform(get("/resumes/" + resumeId + "/sections/" + sectionId)
            .with(user("otheruser").roles("USER")))
            .andExpect(status().isNotFound());

        mockMvc.perform(get("/resumes/" + resumeId + "/sections/" + sectionId)
            .with(user("testuser").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Education"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateSection() throws Exception {
        // Arrange
        SectionRequest request = new SectionRequest("Education");

        String createResponse = mockMvc.perform(post("/resumes/" + resumeId + "/sections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Act
        SectionRequest updateRequest = new SectionRequest("Experience");

        mockMvc.perform(post("/resumes/" + resumeId + "/sections/" + sectionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(updateRequest)))
            .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId + "/sections/" + sectionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Experience"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteSection() throws Exception {
        // Arrange
        SectionRequest request = new SectionRequest("Education");

        String createResponse = mockMvc.perform(post("/resumes/" + resumeId + "/sections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long sectionId = Long.parseLong(createResponse);

        // Act
        mockMvc.perform(post("/resumes/" + resumeId + "/sections/" + sectionId + "/delete"))
            .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId + "/sections/" + sectionId))
            .andExpect(status().isNotFound());
    }
            
    
}
