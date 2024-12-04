package com.coigniez.resumebuilder.model.sectionitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeService;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionService;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRepository;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemService;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionItemServiceIntegrationTest {

    @Autowired
    private SectionItemService sectionItemService;
    @Autowired
    private SectionItemRepository sectionItemRepository;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private SectionRepository sectionRepository;


    private Authentication testuser;
    private Section section;

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

        Long resumeId = resumeService.create(null, resumeRequest);

        Long sectionId = sectionService.create(resumeId, SectionRequest.builder().title("Education").build());
        section = sectionRepository.findById(sectionId).orElseThrow();
    }


    @Test
    void testCreate() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        SectionItemRequest request = new SectionItemRequest(SectionItemType.TEXTBOX.name(), 1, data);

        // Act
        Long sectionItemId = sectionItemService.create(section, request);
        SectionItem sectionItem = sectionItemRepository.findById(sectionItemId).orElseThrow();

        // Assert
        assertNotNull(sectionItemId);
        assertNotNull(sectionItem.getId());
        assertNotNull(sectionItem.getData());
        assertEquals(1, sectionItem.getItemOrder());
        assertEquals(SectionItemType.TEXTBOX, sectionItem.getType());
        assertEquals("This is some example text", ((Textbox) sectionItem.getData()).getContent());
    }

    @Test
    void testDeleteAllBySectionId() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");
        SectionItemRequest request1 = new SectionItemRequest(SectionItemType.TEXTBOX.name(), 1, data);
        sectionItemService.create(section, request1);
        Map<String, Object> data2 = new HashMap<>();
        data2.put("content", "This is some example text");
        SectionItemRequest request2 = new SectionItemRequest(SectionItemType.TEXTBOX.name(), 2, data2);
        sectionItemService.create(section, request2);

        // Act
        sectionItemService.deleteAllBySectionId(section.getId());

        // Assert
        assertEquals(0, sectionItemRepository.count());
    }
}
