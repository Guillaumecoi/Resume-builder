package com.coigniez.resumebuilder.model.layout;

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

import com.coigniez.resumebuilder.model.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.model.resume.ResumeRequest;
import com.coigniez.resumebuilder.model.resume.ResumeService;
import com.coigniez.resumebuilder.model.section.SectionRequest;

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

        resumeId = resumeService.create(null, resumeRequest);
    }

    @Test
    void testCreateAndGetOneColumn() {
        // Arrange
        LayoutRequest layoutDTO = LayoutRequest.builder().build();

        // Act
        Long layoutId = layoutService.create(resumeId, layoutDTO);
        LayoutResponse createdLayout = layoutService.get(layoutId);

        // Assert
        assertNotNull(layoutId);
        assertEquals(layoutId, createdLayout.getId());
        assertEquals(1, createdLayout.getNumberOfColumns());
        assertEquals(1, createdLayout.getColumns().size());
        assertEquals(0.35, createdLayout.getColumnSeparator());
        assertEquals("Executive Suite", createdLayout.getColorScheme().getName());
        assertNotNull(createdLayout.getLatexCommands());

        assertEquals(1, createdLayout.getColumns().get(0).getColumnNumber());
        assertEquals(ColorLocation.DARK_BG, createdLayout.getColumns().get(0).getBackgroundColor());
        assertEquals(ColorLocation.LIGHT_TEXT, createdLayout.getColumns().get(0).getTextColor());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingLeft());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingRight());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingTop());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingBottom());
    }

    @Test
    void testCreateAndGetTwoColumns() {
        // Arrange
        LayoutRequest layoutDTO = LayoutRequest.builder().numberOfColumns(2).build();

        // Act
        Long layoutId = layoutService.create(resumeId, layoutDTO);
        LayoutResponse createdLayout = layoutService.get(layoutId);

        // Assert
        assertNotNull(layoutId);
        assertEquals(layoutId, createdLayout.getId());
        assertEquals(2, createdLayout.getNumberOfColumns());
        assertEquals(2, createdLayout.getColumns().size());
        assertEquals(0.35, createdLayout.getColumnSeparator());
        assertEquals("Executive Suite", createdLayout.getColorScheme().getName());
        assertNotNull(createdLayout.getLatexCommands());

        assertEquals(1, createdLayout.getColumns().get(0).getColumnNumber());
        assertEquals(ColorLocation.DARK_BG, createdLayout.getColumns().get(0).getBackgroundColor());
        assertEquals(ColorLocation.LIGHT_TEXT, createdLayout.getColumns().get(0).getTextColor());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingLeft());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingRight());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingTop());
        assertEquals(10.0, createdLayout.getColumns().get(0).getPaddingBottom());

        assertEquals(2, createdLayout.getColumns().get(1).getColumnNumber());
        assertEquals(ColorLocation.LIGHT_BG, createdLayout.getColumns().get(1).getBackgroundColor());
        assertEquals(ColorLocation.DARK_TEXT, createdLayout.getColumns().get(1).getTextColor());
        assertEquals(10.0, createdLayout.getColumns().get(1).getPaddingLeft());
        assertEquals(10.0, createdLayout.getColumns().get(1).getPaddingRight());
        assertEquals(10.0, createdLayout.getColumns().get(1).getPaddingTop());
        assertEquals(10.0, createdLayout.getColumns().get(1).getPaddingBottom());
    }

}