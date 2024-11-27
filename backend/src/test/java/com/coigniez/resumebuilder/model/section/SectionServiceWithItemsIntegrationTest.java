package com.coigniez.resumebuilder.model.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.model.resume.ResumeRequest;
import com.coigniez.resumebuilder.model.resume.ResumeService;
import com.coigniez.resumebuilder.model.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.model.sectionitem.itemtypes.SectionItemType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionServiceWithItemsIntegrationTest {

    @Autowired
    private SectionService sectionService;
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
    void testCreateAndGet() {
        // Arrange
        List<SectionItemRequest> sectionItems = new ArrayList<>();

        // Add a textbox item to the section
        Map<String, Object> Textbox = new HashMap<>();
        Textbox.put("content", "This is some example text");
        sectionItems.add(new SectionItemRequest(SectionItemType.TEXTBOX.name(), 1, Textbox));

        // Add a skill item to the section
        Map<String, Object> Skill = new HashMap<>();
        Skill.put("name", "Java");
        Skill.put("proficiency", 5);
        sectionItems.add(new SectionItemRequest(SectionItemType.SKILL.name(), 2, Skill));

        SectionRequest request = new SectionRequest(null, "Test Section", sectionItems);

        // Act
        Long sectionId = sectionService.create(resumeId, request);
        SectionResponse response = sectionService.get(sectionId);

        // Assert
        assertNotNull(sectionId);
        assertEquals("Test Section", response.getTitle());
        assertEquals(2, response.getSectionItems().size());
        assertEquals(1, response.getSectionItems().get(0).getItemOrder());
        assertEquals(SectionItemType.SKILL.name(), response.getSectionItems().get(1).getType());
        assertEquals("This is some example text", response.getSectionItems().get(0).getData().get("content"));
        assertEquals("Java", response.getSectionItems().get(1).getData().get("name"));
        assertEquals(5, response.getSectionItems().get(1).getData().get("proficiency"));
    }


}
