package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.*;

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

import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderResp;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageResp;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageUpdateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ColumnHolderServiceIntegrationTest {

    @Autowired
    private ColumnHolderService columnHolderService;

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

        // Create a resume and layout for testing
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Developer")
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .build();

        layoutId = layoutService.create(layoutRequest);
    }

    @Test
    void testCreateAndGetHeader() {
        // Arrange
        HeaderCreateReq request = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        // Act
        Long headerId = columnHolderService.create(request);

        // Assert
        HeaderResp response = (HeaderResp) columnHolderService.get(headerId);
        assertNotNull(response, "Response should not be null");
        assertEquals(headerId, response.getId(), "Header ID should match");
        assertEquals(0.100, response.getHeight(), "Header height should match");
        assertTrue(response.getRepeatOnEveryPage(), "Header repeat should match");
    }

    @Test
    void testCreateAndGetPage() {
        // Arrange
        PageCreateReq request = PageCreateReq.builder()
                .layoutId(layoutId)
                .pageNumber(1)
                .build();

        // Act
        Long pageId = columnHolderService.create(request);

        // Assert
        PageResp response = (PageResp) columnHolderService.get(pageId);
        assertNotNull(response, "Response should not be null");
        assertEquals(pageId, response.getId(), "Page ID should match");
        assertEquals(1, response.getPageNumber(), "Page number should match");
    }

    @Test
    void testHeaderUpdate() {
        // Arrange
        HeaderCreateReq createRequest = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        Long headerId = columnHolderService.create(createRequest);

        // Act
        HeaderUpdateReq updateRequest = HeaderUpdateReq.builder()
                .id(headerId)
                .height(0.200)
                .repeatOnEveryPage(false)
                .build();

        columnHolderService.update(updateRequest);

        // Assert
        HeaderResp response = (HeaderResp) columnHolderService.get(headerId);
        assertEquals(0.200, response.getHeight(), "Header height should be updated");
        assertFalse(response.getRepeatOnEveryPage(), "Header repeat should be updated");
    }

    @Test
    void testPageUpdate() {
        // Arrange
        PageCreateReq createRequest = PageCreateReq.builder()
                .layoutId(layoutId)
                .pageNumber(1)
                .build();

        Long pageId = columnHolderService.create(createRequest);

        // Act
        PageUpdateReq updateRequest = PageUpdateReq.builder()
                .id(pageId)
                .pageNumber(2)
                .build();

        columnHolderService.update(updateRequest);

        // Assert
        PageResp response = (PageResp) columnHolderService.get(pageId);
        assertEquals(2, response.getPageNumber(), "Page number should be updated");
    }

    @Test
    void testDelete() {
        // Arrange
        HeaderCreateReq request = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .build();

        Long headerId = columnHolderService.create(request);

        // Act
        columnHolderService.delete(headerId);

        // Assert
        assertThrows(RuntimeException.class, () -> columnHolderService.get(headerId),
                "Header should not be found after deletion");
    }

    @Test
    void testAccessControl() {
        // Arrange
        HeaderCreateReq request = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .build();

        Long headerId = columnHolderService.create(request);

        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            columnHolderService.create(request);
        }, "Other user should not be able to create a column holder in another user's layout");

        assertThrows(AccessDeniedException.class, () -> {
            columnHolderService.get(headerId);
        }, "Other user should not be able to get another user's column holder");

        assertThrows(AccessDeniedException.class, () -> {
            columnHolderService.update(HeaderUpdateReq.builder().id(headerId).height(0.200).build());
        }, "Other user should not be able to update another user's column holder");

        assertThrows(AccessDeniedException.class, () -> {
            columnHolderService.delete(headerId);
        }, "Other user should not be able to delete another user's column holder");
    }

    @Test
    void testCannotAddMultipleHeaders() {
        // Arrange
        HeaderCreateReq request = HeaderCreateReq.builder()
                .layoutId(layoutId)
                .height(0.100)
                .build();

        columnHolderService.create(request);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            columnHolderService.create(request);
        }, "Should not be able to add multiple headers to a layout");
    }
}