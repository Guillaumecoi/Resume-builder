package com.coigniez.resumebuilder.domain.section;

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

import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeService;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
        sectionItems.add(SectionItemRequest.builder()
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<>() {{
                    put("content", "This is some example text");
                }})
                .build());

        sectionItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .itemOrder(2)
                .data(new HashMap<>() {{
                    put("name", "Java");
                    put("proficiency", 5);
                }})
                .build());

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

        sectionItems.add(SectionItemRequest.builder()
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<>() {{
                    put("content", "This is some example text");
                }})
                .build());

        sectionItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .itemOrder(2)
                .data(new HashMap<>() {{
                    put("name", "Java");
                    put("proficiency", 5);
                }})
                .build());

        SectionRequest request = SectionRequest.builder().title("Test Section").sectionItems(sectionItems).build();

        Long sectionId = sectionService.create(resumeId, request);

        // Update the section
        List<SectionItemRequest> updatedSectionItems = new ArrayList<>();

        // Add a new skill item to the section
        updatedSectionItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .itemOrder(1)
                .data(new HashMap<>() {{
                    put("name", "Python");
                    put("proficiency", 4);
                }})
                .build());

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

        sectionItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .itemOrder(1)
                .data(new HashMap<>() {{
                    put("name", "");
                }})
                .build());

        SectionRequest request = SectionRequest.builder().title("Test Section").sectionItems(sectionItems).build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> sectionService.create(resumeId, request));
    }


}
