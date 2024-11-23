package com.coigniez.resumebuilder.model.resume;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ResumeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateAndGetResume() throws Exception {
        // Arrange
        ResumeRequest createRequest = new ResumeRequest("Software Engineer", "John", "Doe");
    
        // Act - Create
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
                
        Long resumeId = Long.parseLong(createResponse);

        // Assert - Get created resume
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
        void testResumeAccessControl() throws Exception {
        // Create resume as testuser
        ResumeRequest createRequest = new ResumeRequest("Software Engineer", "John", "Doe");
        
        String createResponse = mockMvc.perform(post("/resumes")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
                
        Long resumeId = Long.parseLong(createResponse);

        // Try access with different user - should fail
        mockMvc.perform(get("/resumes/" + resumeId)
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());  // Not forbidden, but not found for security reasons

        // Original user can still access
        mockMvc.perform(get("/resumes/" + resumeId)
                .with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
        }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdate() throws Exception {
        // Arrange
        ResumeRequest createRequest = new ResumeRequest("Software Engineer", "John", "Doe");
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        ResumeRequest updateRequest = new ResumeRequest("Data Scientist", "Alice", "Smith");

        // Act
        mockMvc.perform(post("/resumes/" + resumeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(updateRequest)))
            .andExpect(status().isAccepted());

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Data Scientist"))
            .andExpect(jsonPath("$.firstName").value("Alice"))
            .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUploadPicture() throws Exception {
        // Arrange
        ResumeRequest createRequest = new ResumeRequest("Software Engineer", "John", "Doe");
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    
        Long resumeId = Long.parseLong(createResponse);
    
        // Create mock file with real image content
        Path imagePath = Paths.get("src/test/resources/blue.jpg");
        byte[] imageContent = Files.readAllBytes(imagePath);
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "blue.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageContent
        );
    
        // Act
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/resumes/" + resumeId + "/uploadPicture")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
    
        // Assert
                // Replace the failing assertion with:
        mockMvc.perform(get("/resumes/" + resumeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.picture").value(Base64.getEncoder().encodeToString(file.getBytes())));     
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdatePicture() throws Exception {
        // Arrange
        ResumeRequest createRequest = new ResumeRequest("Software Engineer", "John", "Doe");
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    
        Long resumeId = Long.parseLong(createResponse);
    
        // Create mock file with real image content
        Path imagePath = Paths.get("src/test/resources/blue.jpg");
        byte[] imageContent = Files.readAllBytes(imagePath);
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "blue.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageContent
        );
    
        // Upload picture
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/resumes/" + resumeId + "/uploadPicture")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
    
        // Create mock file with real image content
        Path imagePath2 = Paths.get("src/test/resources/red.jpg");
        byte[] imageContent2 = Files.readAllBytes(imagePath2);
        
        MockMultipartFile file2 = new MockMultipartFile(
            "file",
            "red.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            imageContent2
        );
    
        // Act
        mockMvc.perform(MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/resumes/" + resumeId + "/uploadPicture")
                .file(file2)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
    
        // Assert
        mockMvc.perform(get("/resumes/" + resumeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.picture").value(Base64.getEncoder().encodeToString(file2.getBytes())));

        // Act - update user
        ResumeRequest updateRequest = new ResumeRequest("Data Scientist", "Alice", "Smith");
        mockMvc.perform(post("/resumes/" + resumeId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(updateRequest)))
            .andExpect(status().isAccepted());

        // Assert - picture is still there
        mockMvc.perform(get("/resumes/" + resumeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.picture").value(Base64.getEncoder().encodeToString(file2.getBytes())));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDelete() throws Exception {
        // Arrange
        ResumeRequest createRequest = new ResumeRequest("Software Engineer", "John", "Doe");
        String createResponse = mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long resumeId = Long.parseLong(createResponse);

        // Act
        mockMvc.perform(post("/resumes/" + resumeId + "/delete"))
                .andExpect(status().isAccepted());

        // Assert
        mockMvc.perform(get("/resumes/" + resumeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetAllAndDeleteAll() throws Exception {
        // Arrange
        ResumeRequest createRequest1 = new ResumeRequest("Software Engineer", "John", "Doe");
        mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest1)))
                .andExpect(status().isOk());
    
        ResumeRequest createRequest2 = new ResumeRequest("Data Scientist", "Alice", "Smith");
        mockMvc.perform(post("/resumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest2)))
                .andExpect(status().isOk());
    
        // Act - Get all
        mockMvc.perform(get("/resumes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[1].title").exists())
                .andExpect(jsonPath("$.totalElements").value(2));
    
        // Act - Delete all
        mockMvc.perform(post("/resumes/deleteAll"))
                .andExpect(status().isAccepted());
    
        // Verify deletion
        mockMvc.perform(get("/resumes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

}