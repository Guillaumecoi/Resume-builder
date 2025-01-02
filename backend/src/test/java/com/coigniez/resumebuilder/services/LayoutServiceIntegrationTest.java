package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.dtos.UpdateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LayoutServiceIntegrationTest {

    @Autowired
    private LayoutService layoutService;

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
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        otheruser = new UsernamePasswordAuthenticationToken(
                "otheruser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);

        CreateResumeRequest resumeRequest = CreateResumeRequest.builder().title("Software Developer")
                .sections(List.of(SectionCreateReq.builder().title("Education").build())).build();

        resumeId = resumeService.create(resumeRequest);
    }

    @Test
    void testCreateAndGetOneColumn() {
        // Arrange
        CreateLayoutRequest layoutDTO = CreateLayoutRequest.builder().resumeId(resumeId).build();

        // Act
        Long layoutId = layoutService.create(layoutDTO);

        // Assert
        assertNotNull(layoutId, "Layout ID should not be null");
        LayoutResponse createdLayout = layoutService.get(layoutId);

        assertEquals(layoutId, createdLayout.getId(), "Layout ID should match");
        assertEquals(PageSize.A4, createdLayout.getPageSize(), "Page size should be A4");
        assertEquals(1, createdLayout.getNumberOfColumns(), "Number of columns should be 1");
        assertEquals(0.35, createdLayout.getColumnSeparator(), "Column separator should be 0.35");
        assertEquals("Executive Suite", createdLayout.getColorScheme().getName(),
                "Color scheme should be Executive Suite");
        assertNotNull(createdLayout.getLatexMethods(), "Latex methods should not be null");
    }

    @Test
    void testCreateAndGetTwoColumns() {
        // Arrange
        CreateLayoutRequest layoutRequest = CreateLayoutRequest.builder()
                .resumeId(resumeId)
                .numberOfColumns(2)
                .build();

        // Act
        Long layoutId = layoutService.create(layoutRequest);

        // Assert
        assertNotNull(layoutId, "Layout ID should not be null");
        LayoutResponse createdLayout = layoutService.get(layoutId);

        assertEquals(layoutId, createdLayout.getId(), "Layout ID should match");
        assertEquals(PageSize.A4, createdLayout.getPageSize(), "Page size should be A4");
        assertEquals(2, createdLayout.getNumberOfColumns(), "Number of columns should be 2");
        assertEquals(0.35, createdLayout.getColumnSeparator(), "Column separator should be 0.35");
        assertEquals("Executive Suite", createdLayout.getColorScheme().getName(),
                "Color scheme should be Executive Suite");
        assertNotNull(createdLayout.getLatexMethods(), "Latex methods should not be null");
        assertNotNull(createdLayout.getLatexMethods(), "Latex methods should not be null");
    }

    @Test
    void testAccessControl() {
        // Arrange
        CreateLayoutRequest layoutRequest = CreateLayoutRequest.builder()
                .resumeId(resumeId)
                .numberOfColumns(1)
                .build();

        Long layoutId = layoutService.create(layoutRequest);

        // Act & Assert
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        assertThrows(AccessDeniedException.class, () -> layoutService.create(layoutRequest),
                "User should not be able to create a layout for an others resume");
        assertThrows(AccessDeniedException.class, () -> layoutService.get(layoutId),
                "User should not be able to get a layout for an others resume");
        assertThrows(AccessDeniedException.class,
                () -> layoutService.update(UpdateLayoutRequest.builder().id(layoutId).build()),
                "User should not be able to update a layout for an others resume");
        assertThrows(AccessDeniedException.class, () -> layoutService.delete(layoutId),
                "User should not be able to delete a layout for an others resume");
    }

    @Test
    void testEntityNotFound() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> layoutService.get(-1L),
                "Should throw EntityNotFoundException when layout is not found");
        assertThrows(EntityNotFoundException.class,
                () -> layoutService.update(UpdateLayoutRequest.builder().id(-1L).build()),
                "Should throw EntityNotFoundException when layout is not found");
        assertThrows(EntityNotFoundException.class, () -> layoutService.delete(-1L),
                "Should throw EntityNotFoundException when layout is not found");
        assertThrows(EntityNotFoundException.class, () -> layoutService.generateLatexPdf(-1L),
                "Should throw EntityNotFoundException when layout is not found");
        assertThrows(EntityNotFoundException.class, () -> layoutService.getLatexMethodsMap(-1L),
                "Should throw EntityNotFoundException when layout is not found");
    }

}