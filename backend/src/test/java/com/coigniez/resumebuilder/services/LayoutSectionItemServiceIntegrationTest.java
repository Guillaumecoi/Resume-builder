package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.latex.HasLatexMethod;
import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.CreateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemResponse;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.UpdateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LayoutSectionItemServiceIntegrationTest {

    @Autowired
    private LayoutSectionItemService layoutSectionItemService;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private LayoutService layoutService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private ColumnSectionService columnSectionService;
    @Autowired
    private SectionItemService sectionItemService;
    @Autowired
    private LatexMethodService latexMethodService;

    private Authentication testuser;
    private Authentication otheruser;
    private Long columnSectionId;
    private Long sectionItemId;
    private Long latexMethodId;

    @BeforeEach
    void setUp() {
        // Set up test users
        testuser = new UsernamePasswordAuthenticationToken("testuser", "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        otheruser = new UsernamePasswordAuthenticationToken("otheruser", "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(testuser);

        // Create resume
        Long resumeId = resumeService.create(CreateResumeRequest.builder()
                .title("Test Resume")
                .build());

        // Create layout
        Long layoutId = layoutService.create(CreateLayoutRequest.builder()
                .resumeId(resumeId)
                .build());

        // get column
        Long columnId = layoutService.get(layoutId).getColumns().get(0).getId();

        // Create latex method
        latexMethodId = latexMethodService.create(CreateLatexMethodRequest.builder()
                .layoutId(layoutId)
                .name("Test Method")
                .type(HasLatexMethod.TEXTBOX)
                .method("method")
                .build());

        // Create section
        Long sectionId = sectionService.create(CreateSectionRequest.builder()
                .resumeId(resumeId)
                .title("Test Section")
                .build());

        // Create column section
        columnSectionId = columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .latexMethodId(latexMethodId)
                .defaultOrder(false)
                .build());

        // Create section item
        sectionItemId = sectionItemService.create(CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .item(Textbox.builder().content("Test content").build())
                .build());
    }

    @Test
    void testCreate() {
        // Arrange
        CreateLayoutSectionItemRequest request = CreateLayoutSectionItemRequest.builder()
                .columnSectionId(columnSectionId)
                .sectionItemId(sectionItemId)
                .latexMethodId(latexMethodId)
                .itemOrder(1)
                .build();

        // Act
        Long id = layoutSectionItemService.create(request);

        // Assert
        assertNotNull(id);
        LayoutSectionItemResponse response = layoutSectionItemService.get(id);
        assertEquals(sectionItemId, response.getSectionItem().getId());
        assertEquals(latexMethodId, response.getLatexMethod().getId());
        assertEquals(1, response.getItemOrder());
    }

    @Test
    void testUpdate() {
        // Arrange
        Long id = layoutSectionItemService.create(CreateLayoutSectionItemRequest.builder()
                .columnSectionId(columnSectionId)
                .sectionItemId(sectionItemId)
                .latexMethodId(latexMethodId)
                .itemOrder(1)
                .build());

        UpdateLayoutSectionItemRequest request = UpdateLayoutSectionItemRequest.builder()
                .id(id)
                .sectionItemId(sectionItemId)
                .latexMethodId(latexMethodId)
                .latexMethodId(latexMethodId)
                .itemOrder(2)
                .build();

        // Act
        layoutSectionItemService.update(request);

        // Assert
        LayoutSectionItemResponse response = layoutSectionItemService.get(id);
        assertEquals(2, response.getItemOrder());
    }

    @Test
    void testDelete() {
        // Arrange
        Long id = layoutSectionItemService.create(CreateLayoutSectionItemRequest.builder()
                .columnSectionId(columnSectionId)
                .sectionItemId(sectionItemId)
                .latexMethodId(latexMethodId)
                .itemOrder(1)
                .build());

        // Act
        layoutSectionItemService.delete(id);

        // Assert
        assertThrows(RuntimeException.class, () -> layoutSectionItemService.get(id));
    }

    @Test
    void testGetAllByParentId() {
        // Arrange
        layoutSectionItemService.create(CreateLayoutSectionItemRequest.builder()
                .columnSectionId(columnSectionId)
                .sectionItemId(sectionItemId)
                .latexMethodId(latexMethodId)
                .itemOrder(1)
                .build());

        // Act
        List<LayoutSectionItemResponse> responses = layoutSectionItemService.getAllByParentId(columnSectionId);

        // Assert
        assertEquals(1, responses.size());
    }

    @Test
    void testRemoveAllByParentId() {
        // Arrange
        layoutSectionItemService.create(CreateLayoutSectionItemRequest.builder()
                .columnSectionId(columnSectionId)
                .sectionItemId(sectionItemId)
                .latexMethodId(latexMethodId)
                .itemOrder(1)
                .build());

        // Act
        layoutSectionItemService.removeAllByParentId(columnSectionId);

        // Assert
        List<LayoutSectionItemResponse> responses = layoutSectionItemService.getAllByParentId(columnSectionId);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testAccessControl() {
        // Arrange
        Long id = layoutSectionItemService.create(CreateLayoutSectionItemRequest.builder()
                .columnSectionId(columnSectionId)
                .sectionItemId(sectionItemId)
                .latexMethodId(latexMethodId)
                .itemOrder(1)
                .build());

        // Act & Assert
        SecurityContextHolder.getContext().setAuthentication(otheruser);
        assertThrows(RuntimeException.class, () -> layoutSectionItemService.get(id));
    }
}