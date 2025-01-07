package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResp;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionUpdateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionSimpleCreateReq;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SubSectionServiceIntegrationTest {

    @Autowired
    private SubSectionService subSectionService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private ResumeService resumeService;

    private Authentication testuser;
    private Authentication otheruser;
    private Long sectionId;
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

        // Create a resume with a section
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Developer")
                .sections(List.of(SectionSimpleCreateReq.builder().title("Education").build()))
                .build();

        resumeId = resumeService.create(resumeRequest);
        
        // Create a section for testing
        SectionCreateReq sectionRequest = SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build();

        sectionId = sectionService.create(sectionRequest);
    }

    @Test
    void testCreateAndGet() {
        // Arrange
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection")
                .icon("test-icon")
                .build();

        // Act
        Long subSectionId = subSectionService.create(request);

        // Assert
        assertNotNull(subSectionId, "The subSectionId should not be null after creation");
        SubSectionResp response = subSectionService.get(subSectionId);

        assertNotNull(response, "The subsection should not be null after creation");
        assertEquals(subSectionId, response.getId(), "SubSection id is not correct");
        assertEquals("Test SubSection", response.getTitle(), "SubSection title is not correct");
        assertEquals("test-icon", response.getIcon(), "SubSection icon is not correct");
        assertEquals(true, response.isShowTitle(), "SubSection showTitle is not correct");
        assertTrue(response.getSectionItems().isEmpty(), "SubSection should have no items");
    }

    @Test
    void testUpdate() {
        // Arrange
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection")
                .icon("test-icon")
                .showTitle(true)
                .build();

        Long subSectionId = subSectionService.create(request);

        // Act
        SubSectionUpdateReq updateRequest = SubSectionUpdateReq.builder()
                .id(subSectionId)
                .sectionId(sectionId)
                .title("Updated SubSection")
                .icon("updated-icon")
                .showTitle(false)
                .build();

        subSectionService.update(updateRequest);

        // Assert
        SubSectionResp response = subSectionService.get(subSectionId);

        assertNotNull(response, "The subsection should not be null after update");
        assertEquals(subSectionId, response.getId(), "SubSection id should stay the same after update");
        assertEquals("Updated SubSection", response.getTitle(), "SubSection title should be updated");
        assertEquals("updated-icon", response.getIcon(), "SubSection icon should be updated");
        assertFalse(response.isShowTitle(), "SubSection showTitle should be updated");
    }

    @Test
    void testDelete() {
        // Arrange
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection")
                .build();

        Long subSectionId = subSectionService.create(request);

        // Act
        subSectionService.delete(subSectionId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> {
            subSectionService.get(subSectionId);
        }, "The subsection should not be found after deletion");
    }

    @Test
    void testAuthentication() {
        // Arrange
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection")
                .build();

        Long subSectionId = subSectionService.create(request);

        SubSectionUpdateReq updateRequest = SubSectionUpdateReq.builder()
                .id(subSectionId)
                .sectionId(sectionId)
                .title("Updated SubSection")
                .showTitle(false)
                .build();

        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            subSectionService.create(request);
        }, "Other user should not be able to create a subsection in another user's section");
        
        assertThrows(AccessDeniedException.class, () -> {
            subSectionService.get(subSectionId);
        }, "Other user should not be able to get a subsection from another user's section");
        
        assertThrows(AccessDeniedException.class, () -> {
            subSectionService.update(updateRequest);
        }, "Other user should not be able to update a subsection in another user's section");
        
        assertThrows(AccessDeniedException.class, () -> {
            subSectionService.delete(subSectionId);
        }, "Other user should not be able to delete a subsection from another user's section");
        
        assertThrows(AccessDeniedException.class, () -> {
            subSectionService.getAllByParentId(sectionId);
        }, "Other user should not be able to get subsections from another user's section");
    }

    @Test
    void testAuthenticationOnSectionChange() {
        // Arrange - Create section owned by testuser
        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection")
                .build();
        Long subSectionId = subSectionService.create(request);
    
        // Create resume and section owned by otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);
        ResumeCreateReq otherResumeReq = ResumeCreateReq.builder()
                .title("Other Resume")
                .build();
        Long otherResumeId = resumeService.create(otherResumeReq);
        
        SectionCreateReq otherSectionReq = SectionCreateReq.builder()
                .resumeId(otherResumeId)
                .title("Other Section")
                .build();
        Long otherSectionId = sectionService.create(otherSectionReq);
    
        // Switch back to testuser
        SecurityContextHolder.getContext().setAuthentication(testuser);
    
        // Act & Assert - Try to move subsection to other user's section
        SubSectionUpdateReq updateRequest = SubSectionUpdateReq.builder()
                .id(subSectionId)
                .sectionId(otherSectionId)
                .title("Updated SubSection")
                .showTitle(false)
                .build();
                
        assertThrows(AccessDeniedException.class, () -> {
            subSectionService.update(updateRequest);
        }, "Should not be able to move subsection to another user's section");
    }

    @Test
    void testEntityNotFound() {
        // Arrange
        Long nonExistentId = -1L;

        SubSectionUpdateReq updateRequestWrongId = SubSectionUpdateReq.builder()
                .id(-1L)
                .sectionId(sectionId)
                .title("Updated SubSection")
                .showTitle(false)
                .build();

        SubSectionCreateReq request = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection")
                .build();

        Long subSectionId = subSectionService.create(request);

        SubSectionUpdateReq updateRequestWrongSectionId = SubSectionUpdateReq.builder()
                .id(subSectionId)
                .sectionId(-1L)
                .title("Updated SubSection")
                .showTitle(false)
                .build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            subSectionService.get(nonExistentId);
        }, "SubSection should not be found");

        assertThrows(EntityNotFoundException.class, () -> {
            subSectionService.update(updateRequestWrongId);
        }, "SubSection should not be found");

        assertThrows(EntityNotFoundException.class, () -> {
            subSectionService.update(updateRequestWrongSectionId);
        }, "Section should not be found");

        assertThrows(EntityNotFoundException.class, () -> {
            subSectionService.delete(nonExistentId);
        }, "SubSection should not be found");
    }

    @Test
    void testGetAllByParentId() {
        // Arrange
        SubSectionCreateReq request1 = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection 1")
                .build();

        SubSectionCreateReq request2 = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection 2")
                .build();

        subSectionService.create(request1);
        subSectionService.create(request2);

        // Act
        List<SubSectionResp> subsections = subSectionService.getAllByParentId(sectionId);

        // Assert
        assertEquals(2, subsections.size(), "There should be 2 subsections in the section");
        assertEquals(
            Set.of("Test SubSection 1", "Test SubSection 2"),
            subsections.stream().map(SubSectionResp::getTitle).collect(Collectors.toSet()),
            "The section should have the correct subsections"
        );
    }

    @Test
    void testRemoveAllByParentId() {
        // Arrange
        SubSectionCreateReq request1 = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection 1")
                .build();

        SubSectionCreateReq request2 = SubSectionCreateReq.builder()
                .sectionId(sectionId)
                .title("Test SubSection 2")
                .build();

        subSectionService.create(request1);
        subSectionService.create(request2);

        // Act
        subSectionService.removeAllByParentId(sectionId);

        // Assert
        List<SubSectionResp> subsections = subSectionService.getAllByParentId(sectionId);
        assertEquals(0, subsections.size(), "There should be no subsections in the section");
    }
}