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
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderUpdateReq;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ColumnHolderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long layoutId;

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

        // Create a layout
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .build();

        String layoutResponse = mockMvc.perform(post("/layouts")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(layoutRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        layoutId = Long.parseLong(layoutResponse);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateAndGetHeader() throws Exception {
        // Arrange
        HeaderCreateReq request = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/columnholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long headerId = Long.parseLong(createResponse);

        // Assert - Get created header
        mockMvc.perform(get("/columnholders/" + headerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.height").value(0.100))
                .andExpect(jsonPath("$.repeatOnEveryPage").value(true));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateAndGetPage() throws Exception {
        // Arrange
        PageCreateReq request = PageCreateReq.builder()
                .layoutId(layoutId)
                .pageNumber(1)
                .build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/columnholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long pageId = Long.parseLong(createResponse);

        // Assert - Get created page
        mockMvc.perform(get("/columnholders/" + pageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(1));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateHeader() throws Exception {
        // Arrange
        HeaderCreateReq createRequest = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        String createResponse = mockMvc.perform(post("/columnholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long headerId = Long.parseLong(createResponse);

        // Act - Update
        HeaderUpdateReq updateRequest = HeaderUpdateReq.builder()
                .id(headerId)
                .height(0.200)
                .repeatOnEveryPage(false)
                .build();

        mockMvc.perform(post("/columnholders/" + headerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/columnholders/" + headerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.height").value(0.200))
                .andExpect(jsonPath("$.repeatOnEveryPage").value(false));
    }

    @Test
    void testColumnHolderAccessControl() throws Exception {
        // Arrange - Create column holder as testuser
        HeaderCreateReq request = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .build();

        String createResponse = mockMvc.perform(post("/columnholders")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long columnHolderId = Long.parseLong(createResponse);

        // Assert - Try to access with other user
        mockMvc.perform(get("/columnholders/" + columnHolderId)
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/columnholders/" + columnHolderId)
                .with(user("otheruser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(HeaderUpdateReq.builder()
                        .id(columnHolderId)
                        .height(0.200)
                        .build())))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/columnholders/" + columnHolderId + "/delete")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCannotAddMultipleHeaders() throws Exception {
        // Arrange
        HeaderCreateReq request = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .build();

        // Create first header
        mockMvc.perform(post("/columnholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Try to create second header
        mockMvc.perform(post("/columnholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}