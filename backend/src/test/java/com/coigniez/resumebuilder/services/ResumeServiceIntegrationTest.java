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
import com.coigniez.resumebuilder.domain.resume.ResumeDetailResponse;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeResponse;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionResponse;

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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ResumeServiceIntegrationTest {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private SectionService sectionService;

    private Authentication testuser;
    private Authentication otheruser;

    @BeforeEach
    void setUp() {
        // Create mock users
        testuser = new UsernamePasswordAuthenticationToken(
                "testuser", 
                "password", 
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        otheruser = new UsernamePasswordAuthenticationToken(
                "otheruser", 
                "password", 
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);
    }
    
    @Test
    void testCreateAndGet() {
        // Arrange
        ResumeRequest resumeRequest = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();


        // Act
        Long resumeId = resumeService.create(resumeRequest);
        ResumeDetailResponse resume = resumeService.get(resumeId);

        // Assert
        assertThat(resume).isNotNull();
        assertThat(resume.getId()).isNotNull();
        assertEquals("Software Engineer", resume.getTitle());
        assertNotNull(resume.getCreatedDate());
        assertNotNull(resume.getLastModifiedDate());
        assertThat(resume.getSections()).hasSize(2);
        assertThat(resume.getSections().stream().map(SectionResponse::getTitle))
            .containsExactlyInAnyOrder("Education", "Experience");
    }

    @Test
    void testAddSectionAndGet() {
        // Arrange
        ResumeRequest resumeRequest = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        SectionRequest sectionRequest = SectionRequest.builder()
                .resumeId(resumeId)
                .title("Summary")
                .build();

        // Act
        sectionService.create(sectionRequest);
        ResumeDetailResponse resume = resumeService.get(resumeId);

        // Assert
        assertThat(resume).isNotNull();
        assertThat(resume.getId()).isNotNull();
        assertEquals("Software Engineer", resume.getTitle());
        assertNotNull(resume.getCreatedDate());
        assertNotNull(resume.getLastModifiedDate());
        assertThat(resume.getSections()).hasSize(3);
        assertThat(resume.getSections().stream().map(SectionResponse::getTitle))
        .containsExactlyInAnyOrder("Education", "Experience", "Summary");
    }

    @Test
    void testUploadPicture() throws IOException {
        // Arrange
        ResumeRequest resumeRequest = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);
        MockMultipartFile pictureFile = getPictureFile();

        // Act
        resumeService.uploadPicture(resumeId, pictureFile);
        ResumeDetailResponse resume = resumeService.get(resumeId);

        // Assert
        // Check if the picture is uploaded
        assertNotNull(resume.getPicture());
        assertArrayEquals(pictureFile.getBytes(), resume.getPicture());
        // Check if nothing else is changed
        assertEquals("Software Engineer", resume.getTitle());
        assertThat(resume.getSections()).hasSize(2);
    }

    @Test
    void updateDoesNotRemovePicture() throws IOException {
        // Arrange
        ResumeRequest resumeRequest = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);
        MockMultipartFile pictureFile = getPictureFile();

        // Act
        resumeService.uploadPicture(resumeId, pictureFile);
        resumeService.update(resumeId, ResumeRequest.builder().title("updated").build());
        ResumeDetailResponse resume = resumeService.get(resumeId);

        // Assert
        // Check if the picture is still there
        assertNotNull(resume.getPicture());
        assertArrayEquals(pictureFile.getBytes(), resume.getPicture());
        // Check if the title is updated
        assertEquals("updated", resume.getTitle());
    }

    @Test
    void testUpdate() {
        // Arrange
        ResumeRequest resumeRequest = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        ResumeRequest updatedResumeRequest = ResumeRequest.builder()
                .title("Barista")
                .sections(List.of(
                    SectionRequest.builder().title("CoffeeLover").build()))
                .build();
        // Act
        resumeService.update(resumeId, updatedResumeRequest);
        ResumeDetailResponse updatedResume = resumeService.get(resumeId);

        // Assert
        assertNotNull(updatedResume);
        assertEquals("Barista", updatedResume.getTitle());
        assertThat(updatedResume.getSections()).hasSize(2); // update should not change the sections
    }

    @Test
    void testDelete() {
        // Arrange
        ResumeRequest resumeRequest = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        // Act
        resumeService.delete(resumeId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> { resumeService.get(resumeId); });

    }

    @Test
    void testAuthentications() {
        // Arrange
        ResumeRequest resumeRequest = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);
        
        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> { resumeService.get(resumeId); });
        assertThrows(AccessDeniedException.class, () -> { resumeService.update(resumeId, resumeRequest); });
        assertThrows(AccessDeniedException.class, () -> { resumeService.delete(resumeId); });
    }

    @Test
    void testEntityNotFound() {
        // Arrange
        Long resumeId = -1L;

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> { resumeService.get(resumeId); });
        assertThrows(EntityNotFoundException.class, () -> { resumeService.update(resumeId, ResumeRequest.builder().title("updated").build()); });
        assertThrows(EntityNotFoundException.class, () -> { resumeService.delete(resumeId); });
    }

    @Test
    void testGetAll() {
        // Arrange
        ResumeRequest resumeRequest1 = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();
        ResumeRequest resumeRequest2 = ResumeRequest.builder()
                .title("Barista")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();
        // Act
        Long resumeId1 = resumeService.create(resumeRequest1);
        Long resumeId2 = resumeService.create(resumeRequest2);
        PageResponse<ResumeResponse> resumes = resumeService.getAll(0, 10, "lastModifiedDate");

        // Assert
        assertNotNull(resumes);
        assertEquals(2, resumes.getContent().size());
        // Check the order
        assertEquals(resumeId2, resumes.getContent().get(0).getId()); // Jane was added last so should be first
        assertEquals(resumeId1, resumes.getContent().get(1).getId());
        // Check if resumes are correct
        assertEquals("Software Engineer", resumes.getContent().get(1).getTitle());
        assertEquals("Barista", resumes.getContent().get(0).getTitle());
        // Check the pageresponse
        assertEquals(0, resumes.getNumber());
        assertEquals(10, resumes.getSize());
        assertEquals(2, resumes.getTotalElements());
        assertEquals(1, resumes.getTotalPages());
    }

    @Test
    void testDeleteAll() {
        // Arrange
        ResumeRequest resumeRequest1 = ResumeRequest.builder()
                .title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();
        ResumeRequest resumeRequest2 = ResumeRequest.builder()
                .title("Barista")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        // Act
        Long resumeId1 = resumeService.create(resumeRequest1);
        Long resumeId2 = resumeService.create(resumeRequest2);
        resumeService.deleteAll();

        // Assert
        assertThrows(EntityNotFoundException.class, () -> { resumeService.get(resumeId1); });
        assertThrows(EntityNotFoundException.class, () -> { resumeService.get(resumeId2); });

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
