package com.coigniez.resumebuilder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LayoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long resumeId;

    @BeforeEach
    @WithMockUser(username = "testuser", roles = "USER")
    void setUp() throws Exception {
        // Arrange
        ResumeRequest createRequest = ResumeRequest.builder().title("Software Developer").build();        
    
        // Act - Create
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        resumeId = Long.parseLong(createResponse);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateAndGetLayout() throws Exception {
        // Arrange
        LayoutRequest request = LayoutRequest.builder().resumeId(resumeId).numberOfColumns(1).build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/layouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long layoutId = Long.parseLong(createResponse);

        // Assert - Get created layout
        mockMvc.perform(get("/layouts/" + layoutId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.pageSize").value("A4"))
            .andExpect(jsonPath("$.numberOfColumns").value(1))
            .andExpect(jsonPath("$.columns").isArray())
            .andExpect(jsonPath("$.columnSeparator").value(0.35))
            .andExpect(jsonPath("$.colorScheme").isMap())
            .andExpect(jsonPath("$.latexMethods").isArray())
            .andExpect(jsonPath("$.sectionTitleMethod").isString());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateLayout() throws Exception {
        // Arrange
        LayoutRequest createRequest = LayoutRequest.builder().resumeId(resumeId).numberOfColumns(1).build();
        String createResponse = mockMvc.perform(post("/layouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long layoutId = Long.parseLong(createResponse);

        LayoutRequest updateRequest = LayoutRequest.builder().resumeId(resumeId).columnSeparator(0.4).build();

        // Act
        mockMvc.perform(post("/layouts/" + layoutId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(updateRequest)))
            .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/layouts/" + layoutId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.columnSeparator").value(0.4));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteLayout() throws Exception {
        // Arrange
        LayoutRequest createRequest = LayoutRequest.builder().resumeId(resumeId).numberOfColumns(1).build();
        String createResponse = mockMvc.perform(post("/layouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long layoutId = Long.parseLong(createResponse);

        // Act
        mockMvc.perform(post("/layouts/" + layoutId + "/delete"))
            .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/layouts/" + layoutId))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGenerateLatexPdf() throws Exception {
        // Arrange
        LayoutRequest createRequest = LayoutRequest.builder().resumeId(resumeId).numberOfColumns(1).build();
        String createResponse = mockMvc.perform(post("/layouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long layoutId = Long.parseLong(createResponse);

        // Act & Assert
        mockMvc.perform(get("/layouts/" + layoutId + "/pdf"))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=layout_" + layoutId +".pdf"))
            .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetLatexMethods() throws Exception {
        // Arrange
        LayoutRequest createRequest = LayoutRequest.builder().resumeId(resumeId).numberOfColumns(1).build();
        String createResponse = mockMvc.perform(post("/layouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long layoutId = Long.parseLong(createResponse);

        // Act & Assert
        mockMvc.perform(get("/layouts/" + layoutId + "/methods"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}