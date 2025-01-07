package com.coigniez.resumebuilder.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemUpdateReq;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionCreateReq;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SectionItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Long subSectionId;

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

        Long sectionId = Long.parseLong(sectionResponse);

        // Create a subsection
        SubSectionCreateReq subsectionRequest = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Bachelor's Degree")
                .build();

        String subsectionResponse = mockMvc.perform(post("/subsections")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(subsectionRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        subSectionId = Long.parseLong(subsectionResponse);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreateAndGetSectionItem() throws Exception {
        // Arrange
        SectionItemCreateReq request = SectionItemCreateReq.builder()
                .subSectionId(subSectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("Bachelor of Science in Computer Science").build())
                .build();

        // Act - Create
        String createResponse = mockMvc.perform(post("/section-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionItemId = Long.parseLong(createResponse);

        // Assert - Get created section item
        mockMvc.perform(get("/section-items/" + sectionItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemOrder").value(1))
                .andExpect(jsonPath("$.item.content").value("Bachelor of Science in Computer Science"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateSectionItem() throws Exception {
        // Arrange
        SectionItemCreateReq createRequest = SectionItemCreateReq.builder()
                .subSectionId(subSectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("Bachelor of Science").build())
                .build();

        String createResponse = mockMvc.perform(post("/section-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionItemId = Long.parseLong(createResponse);

        // Act - Update
        SectionItemUpdateReq updateRequest = SectionItemUpdateReq.builder()
                .id(sectionItemId)
                .subSectionId(subSectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("Master of Science").build())
                .build();

        mockMvc.perform(post("/section-items/" + sectionItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Assert
        mockMvc.perform(get("/section-items/" + sectionItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemOrder").value(2))
                .andExpect(jsonPath("$.item.content").value("Master of Science"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteSectionItem() throws Exception {
        // Arrange
        SectionItemCreateReq request = SectionItemCreateReq.builder()
                .subSectionId(subSectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("Bachelor of Science").build())
                .build();

        String createResponse = mockMvc.perform(post("/section-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionItemId = Long.parseLong(createResponse);

        // Act
        mockMvc.perform(post("/section-items/" + sectionItemId + "/delete"))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/section-items/" + sectionItemId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSectionItemAccessControl() throws Exception {
        // Arrange - Create section item as testuser
        SectionItemCreateReq request = SectionItemCreateReq.builder()
                .subSectionId(subSectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("Bachelor of Science").build())
                .build();

        String createResponse = mockMvc.perform(post("/section-items")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long sectionItemId = Long.parseLong(createResponse);

        // Assert - Try to access with other user
        mockMvc.perform(get("/section-items/" + sectionItemId)
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/section-items/" + sectionItemId)
                .with(user("otheruser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        SectionItemUpdateReq.builder()
                                .id(sectionItemId)
                                .subSectionId(subSectionId)
                                .itemOrder(2)
                                .item(Textbox.builder().content("Updated").build())
                                .build())))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/section-items/" + sectionItemId + "/delete")
                .with(user("otheruser").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCreatePicture() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                getTestImage());
    
        SectionItemCreateReq request = SectionItemCreateReq.builder()
                .subSectionId(subSectionId)
                .itemOrder(1)
                .item(Picture.builder().build())
                .build();
    
        MockMultipartFile requestPart = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().writeValueAsString(request).getBytes());
    
        // Act & Assert
        String createResponse = mockMvc.perform(multipart("/section-items/createpicture", subSectionId)
                .file(file)
                .file(requestPart))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber())
                .andReturn().getResponse().getContentAsString();

        long sectionItemId = Long.parseLong(createResponse);

        // Assert picture is uploaded correctly
        mockMvc.perform(get("/section-items/" + sectionItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemOrder").value(1))
                .andExpect(jsonPath("$.item.path").isString())
                .andExpect(jsonPath("$.item.caption").isEmpty());

    }

    private byte[] getTestImage() throws IOException {
        Path resourceDirectory = Paths.get("src", "test", "resources", "blue.jpg");
        return Files.readAllBytes(resourceDirectory);
    }
}