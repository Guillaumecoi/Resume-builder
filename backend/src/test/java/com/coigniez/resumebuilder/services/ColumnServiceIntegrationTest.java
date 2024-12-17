package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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

import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.section.SectionRequest;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ColumnServiceIntegrationTest {

    @Autowired
    private ColumnService columnService;

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

        Long resumeId = resumeService.create(resumeRequest);

        LayoutRequest layoutRequest = LayoutRequest.builder().resumeId(resumeId).numberOfColumns(1).build();
        layoutId = layoutService.create(layoutRequest);
    }

    @Test
    void testCreateAndGetColumn() {
        // Arrange
        ColumnRequest columnRequest = ColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .build();

        // Act
        Long columnId = columnService.create(columnRequest);

        // Assert
        assertNotNull(columnId, "Column ID should not be null");
        ColumnResponse columnResponse = columnService.get(columnId);
        assertEquals(columnId, columnResponse.getId(), "Column ID should match");
        assertEquals(1, columnResponse.getColumnNumber(), "Column number should be 1");
    }

    @Test
    void testUpdateColumn() {
        // Arrange
        ColumnRequest columnRequest = ColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .build();

        Long columnId = columnService.create(columnRequest);

        ColumnRequest updateRequest = ColumnRequest.builder()
                .id(columnId)
                .layoutId(layoutId)
                .columnNumber(2)
                .build();

        // Act
        columnService.update(updateRequest);

        // Assert
        ColumnResponse updatedColumn = columnService.get(columnId);
        assertEquals(2, updatedColumn.getColumnNumber(), "Column number should be updated to 2");
    }

    @Test
    void testDeleteColumn() {
        // Arrange
        ColumnRequest columnRequest = ColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .build();

        Long columnId = columnService.create(columnRequest);

        // Act
        columnService.delete(columnId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> columnService.get(columnId),
                "Column should not be found after deletion");
    }

    @Test
    void testAccessControl() {
        // Arrange
        ColumnRequest columnRequest = ColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .build();

        Long columnId = columnService.create(columnRequest);

        // Act & Assert
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        assertThrows(AccessDeniedException.class, () -> columnService.create(columnRequest),
                "Should not be able to create a column for another user's layout");
        assertThrows(AccessDeniedException.class, () -> columnService.get(columnId),
                "Should not be able to get a column for another user");
        columnRequest.setId(columnId);
        assertThrows(AccessDeniedException.class, () -> columnService.update(columnRequest),
                "Should not be able to update a column for another user");
        assertThrows(AccessDeniedException.class, () -> columnService.delete(columnId),
                "Should not be able to delete a column for another user");
    }
}