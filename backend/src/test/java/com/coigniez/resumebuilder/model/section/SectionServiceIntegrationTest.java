package com.coigniez.resumebuilder.model.section;

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

import com.coigniez.resumebuilder.model.resume.ResumeRequest;
import com.coigniez.resumebuilder.model.resume.ResumeService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;


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

        ResumeRequest resumeRequest = new ResumeRequest("Software Engineer", "John", "Doe", 
                        List.of(new SectionRequest(null, "Education", null), new SectionRequest(null, "Experience", null)));

        resumeId = resumeService.create(null, resumeRequest);
    }

    @Test
    void testCreateAndGet() {
        // Arrange
        SectionRequest request = new SectionRequest(null, "Test Section", null);

        // Act
        Long sectionId = sectionService.create(resumeId, request);
        SectionResponse response = sectionService.get(sectionId);

        // Assert
        assertNotNull(sectionId);
        assertEquals("Test Section", response.getTitle());

    }

    @Test
    void testUpdate() {
        // Arrange
        SectionRequest request = new SectionRequest(null, "Test Section", null);

        Long sectionId = sectionService.create(resumeId, request);

        // Act
        SectionRequest updatedRequest = new SectionRequest(sectionId, "Updated Section", null);
        sectionService.update(sectionId, updatedRequest);
        SectionResponse response = sectionService.get(sectionId);

        // Assert
        assertEquals("Updated Section", response.getTitle());

    }

    @Test
    void testDelete() {
        // Arrange
        SectionRequest request = new SectionRequest(null, "Test Section", null);

        Long sectionId = sectionService.create(resumeId, request);

        // Act
        sectionService.delete(sectionId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> { sectionService.get(sectionId); });

    }

    @Test
    void testAuthentications() {
        // Arrange
        SectionRequest request = new SectionRequest(null, "Test Section", null);

        Long sectionId = sectionService.create(resumeId, request);
        
        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> { sectionService.get(sectionId); });
        assertThrows(AccessDeniedException.class, () -> { sectionService.update(sectionId, request); });
        assertThrows(AccessDeniedException.class, () -> { sectionService.delete(sectionId); });
    }

    @Test
    void testEntityNotFound() {
        // Arrange
        Long sectionId = -1L;

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> { sectionService.get(sectionId); });
        assertThrows(EntityNotFoundException.class, () -> { sectionService.update(sectionId, new SectionRequest(sectionId, null, null)) ; });
        assertThrows(EntityNotFoundException.class, () -> { sectionService.delete(sectionId); });
    }

}
