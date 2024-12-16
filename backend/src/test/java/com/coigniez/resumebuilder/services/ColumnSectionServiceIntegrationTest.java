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

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.section.SectionRequest;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ColumnSectionServiceIntegrationTest {

    @Autowired
    private ColumnSectionService columnSectionService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private LayoutService layoutService;

    private Authentication testuser;
    private Authentication otheruser;
    private Long columnId;
    private Long sectionId;

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

        // Create a resume
        Long resumeId = resumeService.create(ResumeRequest.builder().title("Software Developer").build());

        // Create a layout
        Long layoutId = layoutService.create(LayoutRequest.builder().resumeId(resumeId).build());

        LayoutResponse layout = layoutService.get(layoutId);
        // Create a column
        columnId = layout.getColumns().get(0).getId();

        // Create a section
        sectionId = sectionService.create(SectionRequest.builder().resumeId(resumeId).title("Education").build());
    }

    @Test
    void testCreate() {
        // Arrange
        ColumnSectionRequest request = ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .position(1)
                .build();

        // Act
        Long columnSectionId = columnSectionService.create(request);

        // Assert
        assertNotNull(columnSectionId, "Column section ID should not be null");
    }

    @Test
    void testCreate_DifferentResume() {
        // Arrange
        // Create a new resume
        Long newResumeId = resumeService.create(ResumeRequest.builder().title("Software Developer").build());

        // Create a new section
        Long newSectionId = sectionService.create(SectionRequest.builder().resumeId(newResumeId).title("Experience").build());

        ColumnSectionRequest request = ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(newSectionId)
                .position(1)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> columnSectionService.create(request), 
            "Should throw EntityNotFoundException for section in different resume on create");
    }

    @Test
    void testCreate_NonExistentParent() {
        // Arrange
        ColumnSectionRequest nonExistentColumnRequest = ColumnSectionRequest.builder()
                .columnId(999L) // Non-existent column ID
                .sectionId(sectionId)
                .position(1)
                .build();

        ColumnSectionRequest nonExistentSectionRequest = ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(999L) // Non-existent section ID
                .position(1)
                .build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.create(nonExistentColumnRequest), 
            "Should throw EntityNotFoundException for non-existent column on create");
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.create(nonExistentSectionRequest), 
            "Should throw EntityNotFoundException for non-existent section on create");
    }   

        @Test
    void testUpdate() {
        // Arrange
        ColumnSectionRequest createRequest = ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .position(1)
                .build();

        Long columnSectionId = columnSectionService.create(createRequest);

        ColumnSectionRequest updateRequest = ColumnSectionRequest.builder()
                .id(columnSectionId)
                .columnId(columnId)
                .sectionId(sectionId)
                .position(2)
                .build();

        // Act
        columnSectionService.update(updateRequest);

        // Assert
        ColumnSectionResponse updatedColumnSection = columnSectionService.get(columnSectionId);
        assertEquals(2, updatedColumnSection.getPosition(), "Position should be updated to 2");
    }

    @Test
    void testDelete() {
        // Arrange
        ColumnSectionRequest request = ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .position(1)
                .build();

        Long columnSectionId = columnSectionService.create(request);

        // Act
        columnSectionService.delete(columnSectionId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.get(columnSectionId), 
            "Column section should not be found after deletion");
    }

    @Test
    void testAccessDenied() {
        // Arrange
        ColumnSectionRequest request = ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .position(1)
                .build();

        Long columnSectionId = columnSectionService.create(request);

        // Set the Authentication object to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> columnSectionService.create(request), 
            "Should throw AccessDeniedException for unauthorized access to create");
        assertThrows(AccessDeniedException.class, () -> columnSectionService.get(columnSectionId), 
            "Should throw AccessDeniedException for unauthorized access to get");
        request.setId(columnSectionId);
        assertThrows(AccessDeniedException.class, () -> columnSectionService.update(request), 
            "Should throw AccessDeniedException for unauthorized access to update");
        assertThrows(AccessDeniedException.class, () -> columnSectionService.delete(columnSectionId), 
            "Should throw AccessDeniedException for unauthorized access to delete");
    }

    @Test
    void testEntityNotFound() {
        // Arrange
        ColumnSectionRequest request = ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .position(1)
                .build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.get(999L), 
            "Should throw EntityNotFoundException for non-existent column section on get");
        request.setId(999L);
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.update(request), 
            "Should throw EntityNotFoundException for non-existent column section on update");
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.delete(999L), 
            "Should throw EntityNotFoundException for non-existent column section on delete");
    }
}