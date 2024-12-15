package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.section.SectionRequest;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LayoutServiceIntegrationTest {

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private ResumeService resumeService;

    private Authentication testuser;
    private Long resumeId;

    @BeforeEach
    void setUp() {
        // Create mock users
        testuser = new UsernamePasswordAuthenticationToken(
                "testuser", 
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
    void testCreateAndGetOneColumn() {
        // Arrange
        LayoutRequest layoutDTO = LayoutRequest.builder().resumeId(resumeId).build();

        // Act
        Long layoutId = layoutService.create(layoutDTO);

        // Assert
        assertNotNull(layoutId, "Layout ID should not be null");
        LayoutResponse createdLayout = layoutService.get(layoutId);

        assertEquals(layoutId, createdLayout.getId(), "Layout ID should match");
        assertEquals(PageSize.A4, createdLayout.getPageSize(), "Page size should be A4");
        assertEquals(1, createdLayout.getNumberOfColumns(), "Number of columns should be 1");
        assertEquals(1, createdLayout.getColumns().size(), "Number of columns should be 1");
        assertEquals(0.35, createdLayout.getColumnSeparator(), "Column separator should be 0.35");
        assertEquals("Executive Suite", createdLayout.getColorScheme().getName(), "Color scheme should be Executive Suite");
        assertNotNull(createdLayout.getSectionTitleMethod(), "Section title method should not be null");
        assertNotNull(createdLayout.getLatexMethods(), "Latex methods should not be null");

        assertEquals(1, createdLayout.getColumns().get(0).getColumnNumber(), "Column number should be 1");
        assertEquals(ColorLocation.DARK_BG, createdLayout.getColumns().get(0).getBackgroundColor(), "Background color should be DARK_BG");
        assertEquals(ColorLocation.LIGHT_TEXT, createdLayout.getColumns().get(0).getTextColor(), "Text color should be LIGHT_TEXT");
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingLeft(), "Padding left should be 10.0");
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingRight(), "Padding right should be 10.0");
        assertEquals(20.0, createdLayout.getColumns().get(0).getPaddingTop(), "Padding top should be 20.0");
        assertEquals(20.0, createdLayout.getColumns().get(0).getPaddingBottom(), "Padding bottom should be 20.0");
    }

    @Test
    void testCreateAndGetTwoColumns() {
        // Arrange
        LayoutRequest layoutRequest = LayoutRequest.builder()
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
        assertEquals(2, createdLayout.getColumns().size(), "Number of columns should be 2");
        assertEquals(0.35, createdLayout.getColumnSeparator(), "Column separator should be 0.35");
        assertEquals("Executive Suite", createdLayout.getColorScheme().getName(), "Color scheme should be Executive Suite");
        assertNotNull(createdLayout.getLatexMethods(), "Latex methods should not be null");
        assertNotNull(createdLayout.getLatexMethods(), "Latex methods should not be null");

        assertEquals(1, createdLayout.getColumns().get(0).getColumnNumber(), "Column number should be 1");
        assertEquals(ColorLocation.DARK_BG, createdLayout.getColumns().get(0).getBackgroundColor(), "Background color should be DARK_BG");
        assertEquals(ColorLocation.LIGHT_TEXT, createdLayout.getColumns().get(0).getTextColor(), "Text color should be LIGHT_TEXT");
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingLeft(), "Padding left should be 10.0");
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingRight(), "Padding right should be 10.0");
        assertEquals(20.0, createdLayout.getColumns().get(0).getPaddingTop(), "Padding top should be 20.0");
        assertEquals(20.0, createdLayout.getColumns().get(0).getPaddingBottom(), "Padding bottom should be 20.0");

        assertEquals(2, createdLayout.getColumns().get(1).getColumnNumber(), "Column number should be 2");
        assertEquals(ColorLocation.LIGHT_BG, createdLayout.getColumns().get(1).getBackgroundColor(), "Background color should be LIGHT_BG");
        assertEquals(ColorLocation.DARK_TEXT, createdLayout.getColumns().get(1).getTextColor(), "Text color should be DARK_TEXT");
        assertEquals(10.0, createdLayout.getColumns().get(1).getPaddingLeft(), "Padding left should be 10.0");
        assertEquals(10.0, createdLayout.getColumns().get(1).getPaddingRight(), "Padding right should be 10.0");
        assertEquals(20.0, createdLayout.getColumns().get(1).getPaddingTop(), "Padding top should be 20.0");
        assertEquals(20.0, createdLayout.getColumns().get(1).getPaddingBottom(), "Padding bottom should be 20.0");
    }

}