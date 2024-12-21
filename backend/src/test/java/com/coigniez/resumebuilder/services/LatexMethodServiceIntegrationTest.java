package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

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

import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.latex.dtos.UpdateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.sectionitem.enums.SectionItemType;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LatexMethodServiceIntegrationTest {

    @Autowired
    private LatexMethodService latexMethodService;
    @Autowired
    private LayoutService layoutService;
    @Autowired
    private ResumeService resumeService;

    private Authentication testuser;
    private Authentication otheruser;
    private Long layoutId;

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

        // Create a resume
        Long resumeId = resumeService.create(CreateResumeRequest.builder().title("Software Developer").build());

        // Create a layout
        layoutId = layoutService.create(CreateLayoutRequest.builder().resumeId(resumeId).build());
    }

    @Test
    void testCreate() {
        // Arrange
        CreateLatexMethodRequest request = CreateLatexMethodRequest.builder()
                .layoutId(layoutId)
                .type(SectionItemType.TEXTBOX)
                .name("Test Method")
                .method("Test Content")
                .build();

        // Act
        Long latexMethodId = latexMethodService.create(request);

        // Assert
        assertNotNull(latexMethodId, "Latex method ID should not be null");
    }

    @Test
    void testCreate_NonExistentLayout() {
        // Arrange
        CreateLatexMethodRequest request = CreateLatexMethodRequest.builder()
                .layoutId(999L) // Non-existent layout ID
                .type(SectionItemType.TEXTBOX)
                .name("Test Method")
                .method("Test Content")
                .build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> latexMethodService.create(request),
                "Should throw EntityNotFoundException for non-existent layout");
    }

    @Test
    void testGet() {
        // Arrange
        CreateLatexMethodRequest request = CreateLatexMethodRequest.builder()
                .layoutId(layoutId)
                .type(SectionItemType.TEXTBOX)
                .name("Test Method")
                .method("Test Content")
                .build();

        Long latexMethodId = latexMethodService.create(request);

        // Act
        LatexMethodResponse response = latexMethodService.get(latexMethodId);

        // Assert
        assertNotNull(response, "Latex method response should not be null");
        assertEquals("Test Method", response.getName(), "Latex method name should match");
        assertEquals("Test Content", response.getMethod(), "Latex method content should match");
    }

    @Test
    void testUpdate() {
        // Arrange
        CreateLatexMethodRequest createRequest = CreateLatexMethodRequest.builder()
                .layoutId(layoutId)
                .type(SectionItemType.TEXTBOX)
                .name("Test Method")
                .method("Test Content")
                .build();

        Long latexMethodId = latexMethodService.create(createRequest);

        UpdateLatexMethodRequest updateRequest = UpdateLatexMethodRequest.builder()
                .id(latexMethodId)
                .type(SectionItemType.TEXTBOX)
                .name("Updated Method")
                .method("Updated Content")
                .build();

        // Act
        latexMethodService.update(updateRequest);

        // Assert
        LatexMethodResponse updatedLatexMethod = latexMethodService.get(latexMethodId);
        assertEquals("Updated Method", updatedLatexMethod.getName(), "Latex method name should be updated");
        assertEquals("Updated Content", updatedLatexMethod.getMethod(), "Latex method content should be updated");
    }

    @Test
    void testDelete() {
        // Arrange
        CreateLatexMethodRequest request = CreateLatexMethodRequest.builder()
                .layoutId(layoutId)
                .type(SectionItemType.TEXTBOX)
                .name("Test Method")
                .method("Test Content")
                .build();

        Long latexMethodId = latexMethodService.create(request);

        // Act
        latexMethodService.delete(latexMethodId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> latexMethodService.get(latexMethodId),
                "Latex method should not be found after deletion");
    }

    @Test
    void testAccessDenied() {
        // Arrange
        CreateLatexMethodRequest request = CreateLatexMethodRequest.builder()
                .layoutId(layoutId)
                .type(SectionItemType.TEXTBOX)
                .name("Test Method")
                .method("Test Content")
                .build();

        Long latexMethodId = latexMethodService.create(request);

        // Set the Authentication object to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> latexMethodService.create(request),
                "Should throw AccessDeniedException for unauthorized access to create");
        assertThrows(AccessDeniedException.class, () -> latexMethodService.get(latexMethodId),
                "Should throw AccessDeniedException for unauthorized access to get");
        assertThrows(AccessDeniedException.class,
                () -> latexMethodService.update(UpdateLatexMethodRequest.builder().id(latexMethodId).build()),
                "Should throw AccessDeniedException for unauthorized access to update");
        assertThrows(AccessDeniedException.class, () -> latexMethodService.delete(latexMethodId),
                "Should throw AccessDeniedException for unauthorized access to delete");
    }

    @Test
    void testEntityNotFound() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> latexMethodService.get(999L),
                "Should throw EntityNotFoundException for non-existent latex method on get");
        assertThrows(EntityNotFoundException.class,
                () -> latexMethodService.update(UpdateLatexMethodRequest.builder().id(999L).build()),
                "Should throw EntityNotFoundException for non-existent latex method on update");
        assertThrows(EntityNotFoundException.class, () -> latexMethodService.delete(999L),
                "Should throw EntityNotFoundException for non-existent latex method on delete");
    }
}