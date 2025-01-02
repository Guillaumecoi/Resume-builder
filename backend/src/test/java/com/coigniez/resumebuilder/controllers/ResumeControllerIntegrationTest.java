package com.coigniez.resumebuilder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
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
                                SectionCreateReq.builder().title("Education").build(),
                                SectionCreateReq.builder().title("Experience").build()))
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
    void testUploadPicture() throws Exception {
        // Arrange
        ResumeCreateReq createRequest = ResumeCreateReq.builder().title("Software Engineer").build();
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        // Create mock file with real image content
        Path imagePath = Paths.get("src/test/resources/blue.jpg");
        byte[] imageContent = Files.readAllBytes(imagePath);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "blue.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                imageContent);

        // Act
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/resumes/" + resumeId + "/uploadPicture")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // Assert
        // Replace the failing assertion with:
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.picture").value(Base64.getEncoder().encodeToString(file.getBytes())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testUpdatePicture() throws Exception {
        // Arrange
        ResumeCreateReq createRequest = ResumeCreateReq.builder().title("Software Engineer").build();
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        // Create mock file with real image content
        Path imagePath = Paths.get("src/test/resources/blue.jpg");
        byte[] imageContent = Files.readAllBytes(imagePath);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "blue.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                imageContent);

        // Upload picture
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/resumes/" + resumeId + "/uploadPicture")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // Create mock file with real image content
        Path imagePath2 = Paths.get("src/test/resources/red.jpg");
        byte[] imageContent2 = Files.readAllBytes(imagePath2);

        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "red.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                imageContent2);

        // Act
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/resumes/" + resumeId + "/uploadPicture")
                .file(file2)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.picture").value(Base64.getEncoder().encodeToString(file2.getBytes())));

        // Act - update user
        ResumeCreateReq updateRequest = ResumeCreateReq.builder().title("Software Engineer").build();
        mockMvc.perform(post("/resumes/" + resumeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Assert - picture is still there
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.picture").value(Base64.getEncoder().encodeToString(file2.getBytes())));
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

}