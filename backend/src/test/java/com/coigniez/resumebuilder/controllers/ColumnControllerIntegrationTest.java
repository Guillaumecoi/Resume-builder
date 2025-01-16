package com.coigniez.resumebuilder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnCreateReq;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ColumnControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long columnHolderId;

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

        Long layoutId = Long.parseLong(layoutResponse);

        // Create a column holder (header)
        HeaderCreateReq headerRequest = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        String headerResponse = mockMvc.perform(post("/columnholders")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(headerRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        columnHolderId = Long.parseLong(headerResponse);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateAndGetColumn() throws Exception {
        // Arrange
        ColumnCreateReq request = ColumnCreateReq.builder()
                .columnHolderId(columnHolderId)
                .columnNumber((short) 1)
                .backgroundColor(ColorLocation.LIGHT_BG)
                .textColor(ColorLocation.DARK_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0f)
                .paddingRight(10.0f)
                .paddingTop(20.0f)
                .paddingBottom(20.0f)
                .borderLeft(0.0f)
                .borderRight(0.0f)
                .borderTop(0.0f)
                .borderBottom(0.0f)
                .build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/columns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long columnId = Long.parseLong(createResponse);

        // Assert - Get created column
        mockMvc.perform(get("/columns/" + columnId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.columnNumber").value(1))
                .andExpect(jsonPath("$.backgroundColor").value("LIGHT_BG"))
                .andExpect(jsonPath("$.textColor").value("DARK_TEXT"))
                .andExpect(jsonPath("$.borderColor").value("ACCENT"))
                .andExpect(jsonPath("$.paddingLeft").value(10.0))
                .andExpect(jsonPath("$.paddingRight").value(10.0))
                .andExpect(jsonPath("$.paddingTop").value(20.0))
                .andExpect(jsonPath("$.paddingBottom").value(20.0))
                .andExpect(jsonPath("$.borderLeft").value(0.0))
                .andExpect(jsonPath("$.borderRight").value(0.0))
                .andExpect(jsonPath("$.borderTop").value(0.0))
                .andExpect(jsonPath("$.borderBottom").value(0.0));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateWithPicture() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                getTestImage());

        ColumnCreateReq request = ColumnCreateReq.builder()
                .columnHolderId(columnHolderId)
                .columnNumber((short) 1)
                .paddingLeft(10.0f)
                .paddingRight(10.0f)
                .paddingTop(20.0f)
                .paddingBottom(20.0f)
                .borderLeft(0.0f)
                .borderRight(0.0f)
                .borderTop(0.0f)
                .borderBottom(0.0f)
                .build();

        MockMultipartFile requestPart = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().writeValueAsString(request).getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/columns/createwithpicture")
                .file(file)
                .file(requestPart))
                .andExpect(status().isCreated());
    }

    @Test
    void testColumnAccessControl() throws Exception {
        // Arrange - Create column as testuser
        ColumnCreateReq request = ColumnCreateReq.builder()
                .columnHolderId(columnHolderId)
                .columnNumber((short) 1)
                .build();

        String createResponse = mockMvc.perform(post("/columns")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long columnId = Long.parseLong(createResponse);

        // Assert - Try to access with other user
        mockMvc.perform(get("/columns/" + columnId)
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/columns/" + columnId + "/delete")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());
    }

    // Helper method to get test image
    private byte[] getTestImage() throws Exception {
        Path resourcePath = Paths.get("src", "test", "resources", "blue.jpg");
        return Files.readAllBytes(resourcePath);
    }
}