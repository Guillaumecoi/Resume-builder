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
import com.coigniez.resumebuilder.model.section.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.SectionItemType;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        ResumeRequest resumeRequest = ResumeRequest.builder().title("Software Developer")
                .sections(List.of(SectionRequest.builder().title("Education").build())).build();

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

        SectionRequest request = SectionRequest.builder().title("Test Section").sectionItems(sectionItems).build();

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

    @Test
    void testUpdate() {
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

        SectionRequest request = SectionRequest.builder().title("Test Section").sectionItems(sectionItems).build();

        Long sectionId = sectionService.create(resumeId, request);

        // Update the section
        List<SectionItemRequest> updatedSectionItems = new ArrayList<>();

        // Add a new skill item to the section
        Map<String, Object> Skill2 = new HashMap<>();
        Skill2.put("name", "Python");
        Skill2.put("proficiency", 4);
        updatedSectionItems.add(new SectionItemRequest(SectionItemType.SKILL.name(), 1, Skill2));

        SectionRequest updatedRequest = SectionRequest.builder().title("Updated Section").sectionItems(updatedSectionItems).build();

        // Act
        sectionService.update(sectionId, updatedRequest);
        SectionResponse response = sectionService.get(sectionId);

        // Assert
        assertNotNull(sectionId);
        assertEquals("Updated Section", response.getTitle());
        assertEquals(1, response.getSectionItems().size());
        assertEquals(1, response.getSectionItems().get(0).getItemOrder());
        assertEquals(SectionItemType.SKILL.name(), response.getSectionItems().get(0).getType());
        assertEquals("Python", response.getSectionItems().get(0).getData().get("name"));
        assertEquals(4, response.getSectionItems().get(0).getData().get("proficiency"));
    }

    @Test
    void testValidation() {
        // Arrange
        List<SectionItemRequest> sectionItems = new ArrayList<>();

        // Add a skill item to the section
        Map<String, Object> emptyskill = new HashMap<>();
        emptyskill.put("name", "");
        sectionItems.add(new SectionItemRequest(SectionItemType.SKILL.name(), 1, emptyskill));

        SectionRequest request = SectionRequest.builder().title("Test Section").sectionItems(sectionItems).build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> sectionService.create(resumeId, request));
    }


}
