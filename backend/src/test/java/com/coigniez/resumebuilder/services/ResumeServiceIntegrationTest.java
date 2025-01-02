package com.coigniez.resumebuilder.services;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.common.PageResponse;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeSimpleResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeUpdateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ResumeServiceIntegrationTest {

    @Autowired
    private ResumeService resumeService;

    private Authentication testuser;
    private Authentication otheruser;

    @BeforeEach
    void setUp() {
        // Create mock users
        testuser = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        otheruser = new UsernamePasswordAuthenticationToken(
                "otheruser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);
    }

    @Test
    void testCreateAndGet() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();

        // Act
        Long resumeId = resumeService.create(resumeRequest);
        ResumeResp resume = resumeService.get(resumeId);

        // Assert
        assertNotNull(resume, "Resume should not be null after creation");
        assertNotNull(resume.getId(), "The resume should have an id");
        assertEquals("Software Engineer", resume.getTitle(), "Title should be Software Engineer");
        assertNotNull(resume.getCreatedDate(), "Created date should not be null");
        assertNotNull(resume.getLastModifiedDate(), "Last modified date should not be null");
        assertEquals(resume.getCreatedDate(), resume.getLastModifiedDate(),
                "Created date and last modified date should be the same");
        assertEquals(resume.getSections().size(), 2, "There should be 2 sections");
        assertThat(resume.getSections().stream().map(SectionResp::getTitle))
                .containsExactlyInAnyOrder("Education", "Experience");
    }

    @Test
    void testUploadPicture() throws IOException {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);
        MockMultipartFile pictureFile = getPictureFile();

        // Act
        resumeService.uploadPicture(resumeId, pictureFile);
        ResumeResp resume = resumeService.get(resumeId);

        // Assert
        assertNotNull(resume.getPicture(), "Picture path should not be null after upload");
        assertArrayEquals(pictureFile.getBytes(), resume.getPicture(),
                "Picture content should be the same as the uploaded file");
        assertEquals("Software Engineer", resume.getTitle(), "Title should not be changed");
        assertEquals(2, resume.getSections().size(), "Sections should not be changed");
    }

    @Test
    void updateDoesNotRemovePicture() throws IOException {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);
        MockMultipartFile pictureFile = getPictureFile();

        // Act
        resumeService.uploadPicture(resumeId, pictureFile);
        resumeService.update(ResumeUpdateReq.builder().id(resumeId).title("updated")
                .createSections(List.of()).updateSections(List.of()).build());
        ResumeResp resume = resumeService.get(resumeId);

        // Assert
        assertNotNull(resume.getPicture(), "Picture path should not be null after update");
        assertArrayEquals(pictureFile.getBytes(), resume.getPicture(),
                "Picture content should be the same as the uploaded file");
        assertEquals("updated", resume.getTitle(), "Title should be updated");
    }

    @Test
    void testUpdate() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        ResumeUpdateReq updatedResumeRequest = ResumeUpdateReq.builder()
                .id(resumeId)
                .title("Barista")
                .createSections(List.of(SectionCreateReq.builder().title("CoffeeLover").build()))
                .updateSections(List.of())
                .build();
        // Act
        resumeService.update(updatedResumeRequest);
        ResumeResp updatedResume = resumeService.get(resumeId);

        // Assert
        assertNotNull(updatedResume, "Resume should not be null after update");
        assertEquals("Barista", updatedResume.getTitle(), "Title should be updated");
        assertEquals(3, updatedResume.getSections().size(), "There should be 3 sections");
        assertEquals(Set.of("Education", "Experience", "CoffeeLover"),
                updatedResume.getSections().stream().map(SectionResp::getTitle)
                        .collect(Collectors.toSet()),
                "The section should be added without removing the other sections");
    }

    @Test
    void testDelete() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        // Act
        resumeService.delete(resumeId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId);
        }, "Resume should not be found after deletion");

    }

    @Test
    void testAuthentications() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            resumeService.get(resumeId);
        },
                "Should throw AccessDeniedException when trying to get resume of other user");
        assertThrows(AccessDeniedException.class, () -> {
            resumeService.update(ResumeUpdateReq.builder().id(resumeId).build());
        },
                "Should throw AccessDeniedException when trying to update resume of other user");
        assertThrows(AccessDeniedException.class, () -> {
            resumeService.delete(resumeId);
        },
                "Should throw AccessDeniedException when trying to delete resume of other user");
    }

    @Test
    void testEntityNotFound() {
        // Arrange
        Long resumeId = -1L;

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId);
        },
                "Should throw EntityNotFoundException when resume is not found");
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.update(ResumeUpdateReq.builder().id(resumeId).title("updated").build());
        },
                "Should throw EntityNotFoundException when trying to update non-existing resume");
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.delete(resumeId);
        },
                "Should throw EntityNotFoundException when trying to delete non-existing resume");
    }

    @Test
    void testGetAll() {
        // Arrange
        ResumeCreateReq resumeRequest1 = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();
        ResumeCreateReq resumeRequest2 = ResumeCreateReq.builder()
                .title("Barista")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();
        // Act
        Long resumeId1 = resumeService.create(resumeRequest1);
        Long resumeId2 = resumeService.create(resumeRequest2);
        PageResponse<ResumeSimpleResp> resumes = resumeService.getAll(0, 10, "lastModifiedDate");

        // Assert
        assertNotNull(resumes, "Resumes should not be null");
        assertEquals(2, resumes.getContent().size(), "There should be 2 resumes");
        // Check the order
        assertEquals(resumeId2, resumes.getContent().get(0).getId(),
                "Barista was added last so should be first");
        assertEquals(resumeId1, resumes.getContent().get(1).getId(),
                "Software Engineer was added first so should be last");
        // Check if resumes are correct
        assertEquals("Software Engineer", resumes.getContent().get(1).getTitle(),
                "Title should be Software Engineer");
        assertEquals("Barista", resumes.getContent().get(0).getTitle(), "Title should be Barista");
        // Check the pageresponse
        assertEquals(0, resumes.getNumber(), "Page number should be 0");
        assertEquals(10, resumes.getSize(), "Page size should be 10");
        assertEquals(2, resumes.getTotalElements(), "Total elements should be 2");
        assertEquals(1, resumes.getTotalPages(), "Total pages should be 1");
    }

    @Test
    void testDeleteAll() {
        // Arrange
        ResumeCreateReq resumeRequest1 = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();
        ResumeCreateReq resumeRequest2 = ResumeCreateReq.builder()
                .title("Barista")
                .sections(List.of(
                        SectionCreateReq.builder().title("Education").build(),
                        SectionCreateReq.builder().title("Experience").build()))
                .build();

        // Act
        Long resumeId1 = resumeService.create(resumeRequest1);
        Long resumeId2 = resumeService.create(resumeRequest2);
        resumeService.deleteAll();

        // Assert
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId1);
        }, "Resume 1 should not be found after deletion");
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId2);
        }, "Resume 2 should not be found after deletion");

    }

    private MockMultipartFile getPictureFile() throws IOException {
        // Create mock file with real image content
        Path imagePath = Paths.get("src/test/resources/blue.jpg");
        byte[] imageContent = Files.readAllBytes(imagePath);

        return new MockMultipartFile(
                "file",
                "blue.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                imageContent);
    }

}
