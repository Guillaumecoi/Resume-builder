package com.coigniez.resumebuilder.services;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.resume.ResumeDetailResponse;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionServiceIntegrationTest {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private ResumeService resumeService;

    private Authentication testuser;
    private Authentication otheruser;
    private Long resumeId;

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

        ResumeRequest resumeRequest = ResumeRequest.builder().title("Software Developer")
                .sections(List.of(SectionRequest.builder().title("Education").build())).build();

        resumeId = resumeService.create(resumeRequest);
    }

    @Test
    void testCreateAndGet() {
        // Arrange
        SectionRequest request = SectionRequest.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();

        // Act
        Long sectionId = sectionService.create(request);

        // Assert
        assertNotNull(sectionId, "The sectionId should not be null after creation");
        SectionResponse response = sectionService.get(sectionId);
        ResumeDetailResponse resume = resumeService.get(resumeId);

        assertNotNull(response, "The section should not be null after creation");
        assertEquals(sectionId, response.getId(), "Section id is not correct");
        assertEquals("Test Section", response.getTitle(), "Section title is not correct");
        assertEquals(2, resume.getSections().size(), "The resume should have 2 sections after creation of the new section");
        assertEquals(Set.of("Education", "Test Section"), 
            resume.getSections().stream().map(SectionResponse::getTitle).collect(Collectors.toSet()),
            "The resume should have the correct sections after creation of the new section");

    }

    @Test
    void testUpdate() {
        // Arrange
        SectionRequest request = SectionRequest.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();

        Long sectionId = sectionService.create(request);

        // Act
        SectionRequest updatedRequest = SectionRequest.builder().id(sectionId).title("Updated Section").build();
        sectionService.update(updatedRequest);

        // Assert
        SectionResponse response = sectionService.get(sectionId);

        assertNotNull(response, "The section should not be null after update");
        assertEquals(sectionId, response.getId(), "Section id should stay the same after update");
        assertEquals("Updated Section", response.getTitle(), "Section title should be updated");

    }

    @Test
    void testDelete() {
        // Arrange
        SectionRequest request = SectionRequest.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();

        Long sectionId = sectionService.create(request);

        // Act
        sectionService.delete(sectionId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> { sectionService.get(sectionId); }, "The section should not be found after deletion");

    }

    @Test
    void testAuthentications() {
        // Arrange
        SectionRequest request = SectionRequest.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();

        Long sectionId = sectionService.create(request);
        
        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> { sectionService.create(request); },
            "Other user should not be able to create a section in another user's resume");
        assertThrows(AccessDeniedException.class, () -> { sectionService.get(sectionId); },
            "Other user should not be able to get a section in another user's resume");
        request.setId(sectionId);
        assertThrows(AccessDeniedException.class, () -> { sectionService.update(request); },
            "Other user should not be able to update a section in another user's resume");
        assertThrows(AccessDeniedException.class, () -> { sectionService.delete(sectionId); },
            "Other user should not be able to delete a section in another user's resume");
    }

    @Test
    void testEntityNotFound() {
        // Arrange
        Long sectionId = -1L;

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> { sectionService.get(sectionId); }, "Section should not be found");
        assertThrows(EntityNotFoundException.class, () -> { sectionService.update(SectionRequest.builder().id(sectionId).build()); },
            "Section should not be found");
        assertThrows(EntityNotFoundException.class, () -> { sectionService.delete(sectionId); },
            "Section should not be found");
    }

}
