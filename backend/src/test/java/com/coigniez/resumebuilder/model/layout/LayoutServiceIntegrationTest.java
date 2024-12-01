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

        ResumeRequest resumeRequest = new ResumeRequest("Software Engineer", "John", "Doe", 
                        List.of(new SectionRequest(null, "Education", null), new SectionRequest(null, "Experience", null)));

        resumeId = resumeService.create(null, resumeRequest);
    }

    @Test
    void testCreateAndGetOneColumn() {
        // Arrange
        LayoutDTO layoutDTO = LayoutDTO.builder().build();

        // Act
        Long layoutId = layoutService.create(resumeId, layoutDTO);
        LayoutDTO createdLayout = layoutService.get(layoutId);

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
        assertEquals(layoutId, createdLayout.getColumns().get(0).getLayoutId());

    }

}