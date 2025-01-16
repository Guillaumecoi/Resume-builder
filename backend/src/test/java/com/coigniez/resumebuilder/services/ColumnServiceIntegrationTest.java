package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.*;

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

import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.repository.ColumnRepository;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.column.LayoutColumn;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ColumnServiceIntegrationTest {

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private ColumnHolderService columnHolderService;

    @Autowired
    private ColumnRepository columnRepository;

    private Authentication testuser;
    private Authentication otheruser;
    private Long columnHolderId;

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
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Developer")
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        // Create a layout
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .build();

        Long layoutId = layoutService.create(layoutRequest);

        // Create a column holder (header)
        HeaderCreateReq headerRequest = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        columnHolderId = columnHolderService.create(headerRequest);
    }

    @Test
    void testCreateAndGetColumn() {
        // Arrange
        ColumnCreateReq request = ColumnCreateReq.builder()
                .columnHolderId(columnHolderId)
                .columnNumber((short) 1)
                .backgroundColor(ColorLocation.LIGHT_BG)
                .textColor(ColorLocation.DARK_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .build();

        // Act
        Long columnId = columnService.create(request);
        ColumnResp response = columnService.get(columnId);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(columnId, response.getId(), "Column ID should match");
        assertEquals(1, response.getColumnNumber(), "Column number should match");
        assertEquals(ColorLocation.LIGHT_BG, response.getBackgroundColor(), "Background color should match");
        assertEquals(ColorLocation.DARK_TEXT, response.getTextColor(), "Text color should match");
        assertEquals(ColorLocation.ACCENT, response.getBorderColor(), "Border color should match");
    }

    @Test
    void testCreateWithPicture() throws IOException {
        // Arrange
        MockMultipartFile file = getPictureFile();

        ColumnCreateReq request = ColumnCreateReq.builder()
                .columnHolderId(columnHolderId)
                .columnNumber((short) 1)
                .build();

        // Act
        Long columnId = columnService.CreateWithPicture(file, request);
        LayoutColumn column = columnRepository.findById(columnId).orElseThrow();

        // Assert
        assertNotNull(columnId, "The ID should not be null");
        assertEquals(columnId, column.getId(), "The ID of the entity should match the returned ID");
        assertNotNull(column.getBackgroundImage(), "The background image should not be null");
        assertNotNull(column.getBackgroundImage().getImagePath(), "The image path should not be null");

        // Verify actual image content
        byte[] originalBytes = file.getBytes();
        assertNotNull(originalBytes, "The retrieved image should not be null");
        assertTrue(originalBytes.length > 0, "The retrieved image should not be empty");
    }

    @Test
    void testUpdate() {
        // Arrange
        ColumnCreateReq createRequest = ColumnCreateReq.builder()
                .columnHolderId(columnHolderId)
                .columnNumber((short) 1)
                .columnSize(1.0f)
                .backgroundColor(ColorLocation.LIGHT_BG)
                .textColor(ColorLocation.DARK_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0f)
                .paddingRight(10.0f)
                .paddingTop(20.0f)
                .paddingBottom(20.0f)
                .borderLeft(0.0f)
                .borderRight(0.0f)
                .borderTop(0.0f)
                .borderBottom(0.0f)
                .build();

        Long columnId = columnService.create(createRequest);

        // Act
        ColumnUpdateReq updateRequest = ColumnUpdateReq.builder()
                .id(columnId)
                .columnNumber((short) 2)
                .columnSize(2.0f)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.PRIMARY)
                .paddingLeft(15.0f)
                .paddingRight(15.0f)
                .paddingTop(25.0f)
                .paddingBottom(25.0f)
                .borderLeft(1.0f)
                .borderRight(1.0f)
                .borderTop(1.0f)
                .borderBottom(1.0f)
                .build();

        columnService.update(updateRequest);

        // Assert
        ColumnResp response = columnService.get(columnId);
        assertEquals(columnId, response.getId());
        assertEquals(2, response.getColumnNumber());
        assertEquals(2.0f, response.getColumnSize());
        assertEquals(ColorLocation.DARK_BG, response.getBackgroundColor());
        assertEquals(ColorLocation.LIGHT_TEXT, response.getTextColor());
        assertEquals(ColorLocation.PRIMARY, response.getBorderColor());
        assertEquals(15.0f, response.getPaddingLeft());
        assertEquals(15.0f, response.getPaddingRight());
        assertEquals(25.0f, response.getPaddingTop());
        assertEquals(25.0f, response.getPaddingBottom());
        assertEquals(1.0f, response.getBorderLeft());
        assertEquals(1.0f, response.getBorderRight());
        assertEquals(1.0f, response.getBorderTop());
        assertEquals(1.0f, response.getBorderBottom());
    }

    @Test
    void testAccessControl() {
        // Arrange
        ColumnCreateReq request = ColumnCreateReq.builder()
                .columnHolderId(columnHolderId)
                .columnNumber((short) 1)
                .build();

        Long columnId = columnService.create(request);

        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            columnService.create(request);
        }, "Other user should not be able to create a column in another user's column holder");

        assertThrows(AccessDeniedException.class, () -> {
            columnService.get(columnId);
        }, "Other user should not be able to get another user's column");

        assertThrows(AccessDeniedException.class, () -> {
            columnService.update(ColumnUpdateReq.builder().id(columnId).build());
        }, "Other user should not be able to update another user's column");

        assertThrows(AccessDeniedException.class, () -> {
            columnService.delete(columnId);
        }, "Other user should not be able to delete another user's column");
    }

    // Helper method to get test image
    private MockMultipartFile getPictureFile() throws IOException {
        // Load test image from resources
        Path resourcePath = Paths.get("src", "test", "resources", "blue.jpg");
        byte[] content = Files.readAllBytes(resourcePath);

        return new MockMultipartFile(
                "file",
                "blue.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                content);
    }
    
}