package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResp;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutUpdateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;

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

        ResumeCreateReq resumeRequest = ResumeCreateReq.builder().title("Software Developer").build();

        resumeId = resumeService.create(resumeRequest);
    }

    @Test
    void testCreateAndGetWithHeaderFooter() {
        // Arrange
        HeaderCreateReq header = HeaderCreateReq.builder()
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

    
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .header(header)
                .build();
    
        // Act
        Long layoutId = layoutService.create(layoutRequest);
    
        // Assert
        LayoutResp createdLayout = layoutService.get(layoutId);
        assertNotNull(createdLayout.getHeader(), "Header should not be null");
        assertEquals(0.100, createdLayout.getHeader().getHeight(), "Header height should match");
        assertTrue(createdLayout.getHeader().getRepeatOnEveryPage(), "Header repeat should match");
    }
    
    @Test
    void testCreateAndGetWithPages() {
        // Arrange
        List<PageCreateReq> pages = List.of(
            PageCreateReq.builder()
                .pageNumber(1)
                .build(),
            PageCreateReq.builder()
                .pageNumber(2)
                .build()
        );
    
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .pages(pages)
                .build();
    
        // Act
        Long layoutId = layoutService.create(layoutRequest);
    
        // Assert
        LayoutResp createdLayout = layoutService.get(layoutId);
        assertNotNull(createdLayout.getPages(), "Pages should not be null");
        assertEquals(2, createdLayout.getPages().size(), "Should have 2 pages");
        assertEquals(1, createdLayout.getPages().get(0).getPageNumber(), "First page number should be 1");
        assertEquals(2, createdLayout.getPages().get(1).getPageNumber(), "Second page number should be 2");
    }
    
    @Test
    void testUpdateHeaderFooter() {
        // Arrange
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .header(HeaderCreateReq.builder().height(0.100).build())
                .build();
    
        Long layoutId = layoutService.create(layoutRequest);
        LayoutResp createdLayout = layoutService.get(layoutId);
    
        // Act
        LayoutUpdateReq updateRequest = LayoutUpdateReq.builder()
                .id(layoutId)
                .header(HeaderUpdateReq.builder().id(createdLayout.getHeader().getId()).height(0.200).build())
                .build();
    
        layoutService.update(updateRequest);
    
        // Assert
        LayoutResp updatedLayout = layoutService.get(layoutId);
        assertEquals(0.200, updatedLayout.getHeader().getHeight(), "Header height should be updated");
    }

    @Test
    void testAccessControl() {
        // Arrange
        LayoutCreateReq layoutRequest = LayoutCreateReq.builder()
                .resumeId(resumeId)
                .build();

        Long layoutId = layoutService.create(layoutRequest);

        // Act & Assert
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        assertThrows(AccessDeniedException.class, () -> layoutService.create(layoutRequest),
                "User should not be able to create a layout for an others resume");
        assertThrows(AccessDeniedException.class, () -> layoutService.get(layoutId),
                "User should not be able to get a layout for an others resume");
        assertThrows(AccessDeniedException.class,
                () -> layoutService.update(LayoutUpdateReq.builder().id(layoutId).build()),
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
                () -> layoutService.update(LayoutUpdateReq.builder().id(-1L).build()),
                "Should throw EntityNotFoundException when layout is not found");
        assertThrows(EntityNotFoundException.class, () -> layoutService.delete(-1L),
                "Should throw EntityNotFoundException when layout is not found");
        assertThrows(EntityNotFoundException.class, () -> layoutService.generateLatexPdf(-1L),
                "Should throw EntityNotFoundException when layout is not found");
        assertThrows(EntityNotFoundException.class, () -> layoutService.getLatexMethodsMap(-1L),
                "Should throw EntityNotFoundException when layout is not found");
    }

}