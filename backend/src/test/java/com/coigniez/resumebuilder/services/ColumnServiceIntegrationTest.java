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

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.column.dtos.UpdateColumnRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;

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
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        otheruser = new UsernamePasswordAuthenticationToken(
                "otheruser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);

        ResumeCreateReq resumeRequest = ResumeCreateReq.builder().title("Software Developer")
                .sections(List.of(SectionCreateReq.builder().title("Education").build())).build();

        Long resumeId = resumeService.create(resumeRequest);

        CreateLayoutRequest layoutRequest = CreateLayoutRequest.builder().resumeId(resumeId).numberOfColumns(1)
                .build();
        layoutId = layoutService.create(layoutRequest);
    }

    @Test
    void testCreateAndGetColumn() {
        // Arrange
        CreateColumnRequest columnRequest = CreateColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.ACCENT)
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
        CreateColumnRequest columnRequest = CreateColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .build();

        Long columnId = columnService.create(columnRequest);

        UpdateColumnRequest updateRequest = UpdateColumnRequest.builder()
                .id(columnId)
                .columnNumber(2)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .createSectionMappings(List.of())
                .updateSectionMappings(List.of())
                .borderTop(0.0)
                .borderRight(0.0)
                .borderBottom(0.0)
                .borderLeft(0.0)
                .paddingTop(0.0)
                .paddingRight(0.0)
                .paddingBottom(0.0)
                .paddingLeft(0.0)
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
        CreateColumnRequest columnRequest = CreateColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.ACCENT)
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
        CreateColumnRequest columnRequest = CreateColumnRequest.builder()
                .layoutId(layoutId)
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .build();

        Long columnId = columnService.create(columnRequest);

        // Act & Assert
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        assertThrows(AccessDeniedException.class, () -> columnService.create(columnRequest),
                "Should not be able to create a column for another user's layout");
        assertThrows(AccessDeniedException.class, () -> columnService.get(columnId),
                "Should not be able to get a column for another user");
        assertThrows(AccessDeniedException.class,
                () -> columnService.update(UpdateColumnRequest.builder().id(columnId).build()),
                "Should not be able to update a column for another user");
        assertThrows(AccessDeniedException.class, () -> columnService.delete(columnId),
                "Should not be able to delete a column for another user");
    }
}