package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

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
import org.springframework.validation.annotation.Validated;

import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.dtos.UpdateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.repository.ColumnSectionRepository;
import com.coigniez.resumebuilder.templates.color.ColorTemplates;
import com.coigniez.resumebuilder.templates.methods.LatexMethodTemplates;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Validated
public class ColumnSectionServiceIntegrationTest {

    @Autowired
    private ColumnSectionService columnSectionService;
    @Autowired
    private ColumnSectionRepository columnSectionRepository;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private LayoutService layoutService;

    private Authentication testuser;
    private Authentication otheruser;
    private Long columnId;
    private Long sectionId;

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

        // Create a resume
        Long resumeId = resumeService.create(ResumeCreateReq.builder().title("Software Developer").build());

        // Create a layout
        Long layoutId = layoutService.create(CreateLayoutRequest.builder()
                .resumeId(resumeId)
                .numberOfColumns(1)
                .columns(List.of(
                        CreateColumnRequest.builder()
                                .columnNumber(1)
                                .backgroundColor(ColorLocation.LIGHT_BG)
                                .textColor(ColorLocation.DARK_TEXT)
                                .borderColor(ColorLocation.ACCENT)
                                .borderRight(2.0)
                                .build(),
                        CreateColumnRequest.builder()
                                .columnNumber(2)
                                .backgroundColor(ColorLocation.LIGHT_BG)
                                .textColor(ColorLocation.DARK_TEXT)
                                .borderColor(ColorLocation.ACCENT)
                                .build()))
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .latexMethods(LatexMethodTemplates.getStandardMethods())
                .build());

        LayoutResponse layout = layoutService.get(layoutId);
        // Create a column
        columnId = layout.getColumns().get(0).getId();

        // Create a section
        sectionId = sectionService
                .create(SectionCreateReq.builder().resumeId(resumeId).title("Education").build());
    }

    @Test
    void testCreate() {
        // Arrange
        CreateColumnSectionRequest request = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        // Act
        Long columnSectionId = columnSectionService.create(request);

        // Assert
        assertNotNull(columnSectionId, "Column section ID should not be null");
    }

    @Test
    void testCreate_DifferentResume() {
        // Arrange
        // Create a new resume
        Long newResumeId = resumeService
                .create(ResumeCreateReq.builder().title("Software Developer").build());

        // Create a new section
        Long newSectionId = sectionService.create(
                SectionCreateReq.builder().resumeId(newResumeId).title("Experience").build());

        CreateColumnSectionRequest request = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(newSectionId)
                .itemOrder(1)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> columnSectionService.create(request),
                "Should throw EntityNotFoundException for section in different resume on create");
    }

    @Test
    void testCreate_NonExistentParent() {
        // Arrange
        CreateColumnSectionRequest nonExistentColumnRequest = CreateColumnSectionRequest.builder()
                .columnId(-1L) // Non-existent column ID
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        CreateColumnSectionRequest nonExistentSectionRequest = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(-1L) // Non-existent section ID
                .itemOrder(1)
                .build();

        CreateColumnSectionRequest nonExistentMethodRequest = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .latexMethodId(-1L) // Non-existent latex method ID
                .itemOrder(1)
                .build();

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.create(nonExistentColumnRequest),
                "Should throw EntityNotFoundException for non-existent column on create");
        assertThrows(EntityNotFoundException.class,
                () -> columnSectionService.create(nonExistentSectionRequest),
                "Should throw EntityNotFoundException for non-existent section on create");
        assertThrows(EntityNotFoundException.class,
                () -> columnSectionService.create(nonExistentMethodRequest),
                "Should throw EntityNotFoundException for non-existent latex method on create");
    }

    @Test
    void testCreate_AutoIncrementSectionOrder() {
        // Arrange
        CreateColumnSectionRequest request1 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .build();

        CreateColumnSectionRequest request2 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .build();

        CreateColumnSectionRequest request3 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .build();

        // Act
        long id1 = columnSectionService.create(request1);
        long id2 = columnSectionService.create(request2);
        long id3 = columnSectionService.create(request3);

        // Assert
        List<ColumnSection> items = columnSectionRepository.findAllByColumnId(columnId);
        items.sort(Comparator.comparing(ColumnSection::getSectionOrder)); // Sort by section order for easier
                                                                       // comparison

        assertEquals(3, items.size(), "There should be 3 items");
        assertEquals(List.of(1, 2, 3),
                items.stream().map(ColumnSection::getSectionOrder).collect(Collectors.toList()),
                "Section orders should be 1, 2, 3");
        assertEquals(id1, items.get(0).getId(), "First item should have ID of id1");
        assertEquals(id2, items.get(1).getId(), "Second item should have ID of id2");
        assertEquals(id3, items.get(2).getId(), "Third item should have ID of id3");
    }

    @Test
    void testCreate_IncrementsSectionOrder() {
        // Arrange
        CreateColumnSectionRequest request1 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        long id1 = columnSectionService.create(request1);

        CreateColumnSectionRequest request2 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(2)
                .build();

        long id2 = columnSectionService.create(request2);

        // Act
        CreateColumnSectionRequest request3 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(2)
                .build();

        long id3 = columnSectionService.create(request3);

        CreateColumnSectionRequest request4 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(4)
                .build();

        long id4 = columnSectionService.create(request4);

        // Assert
        List<ColumnSection> items = columnSectionRepository.findAllByColumnId(columnId);
        items.sort(Comparator.comparing(ColumnSection::getSectionOrder)); // Sort by section order for easier
                                                                       // comparison

        assertEquals(4, items.size(), "There should be 4 items");
        assertEquals(List.of(1, 2, 3, 4),
                items.stream().map(ColumnSection::getSectionOrder).collect(Collectors.toList()),
                "Section orders should be 1, 2, 3, 4");
        assertEquals(id1, items.get(0).getId(), "First item should have ID of id1");
        assertEquals(id3, items.get(1).getId(), "Second item should have ID of id3");
        assertEquals(id2, items.get(2).getId(), "Third item should have ID of id2");
        assertEquals(id4, items.get(3).getId(), "Fourth item should have ID of id4");
    }

    @Test
    void testUpdate() {
        // Arrange
        CreateColumnSectionRequest createRequest = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        Long columnSectionId = columnSectionService.create(createRequest);
        ColumnSectionResponse columnSection = columnSectionService.get(columnSectionId);

        UpdateColumnSectionRequest updateRequest = UpdateColumnSectionRequest.builder()
                .id(columnSectionId)
                .latexMethodId(columnSection.getLatexMethod().getId())
                .itemOrder(2)
                .endsep(2.0)
                .itemsep(2.0)
                .build();

        // Act
        columnSectionService.update(updateRequest);

        // Assert
        ColumnSectionResponse updatedColumnSection = columnSectionService.get(columnSectionId);
        assertEquals(2, updatedColumnSection.getSectionOrder(), "itemOrder should be updated to 2");
        assertEquals(2.0, updatedColumnSection.getEndsep(), "endsep should be updated to 2.0");
        assertEquals(2.0, updatedColumnSection.getItemsep(), "itemsep should be updated to 2.0");
    }

    @Test
    void testUpdate_WithoutId() {
        // Arrange
        UpdateColumnSectionRequest request = UpdateColumnSectionRequest.builder()
                .itemOrder(1)
                .build();

        // Act & Assert
        assertNull(request.getId(), "ID should be null before update");
        assertThrows(ConstraintViolationException.class, () -> columnSectionService.update(request),
                "Should throw ConstraintViolationException for missing ID on update");
    }

    @Test
    void testUpdate_IncrementsSectionOrder() {
        // Arrange
        CreateColumnSectionRequest request1 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        long id1 = columnSectionService.create(request1);

        CreateColumnSectionRequest request2 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(2)
                .build();

        long id2 = columnSectionService.create(request2);

        CreateColumnSectionRequest request3 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(3)
                .build();

        long id3 = columnSectionService.create(request3);
        ColumnSectionResponse columnSection = columnSectionService.get(id3);

        // Act
        UpdateColumnSectionRequest request4 = UpdateColumnSectionRequest.builder()
                .id(id3)
                .latexMethodId(columnSection.getLatexMethod().getId())
                .itemOrder(2)
                .endsep(2.0)
                .itemsep(2.0)
                .build();

        columnSectionService.update(request4);

        // Assert
        List<ColumnSection> items = columnSectionRepository.findAllByColumnId(columnId);
        // Sort by section order for easier comparison
        items.sort(Comparator.comparing(ColumnSection::getSectionOrder));

        assertEquals(3, items.size(), "There should be 3 items");
        assertEquals(List.of(1, 2, 3),
                items.stream().map(ColumnSection::getSectionOrder).collect(Collectors.toList()),
                "Section orders should be 1, 2, 3");
        assertEquals(id1, items.get(0).getId(), "First item should have ID of id1");
        assertEquals(id3, items.get(1).getId(), "Second item should have ID of id3");
        assertEquals(id2, items.get(2).getId(), "Third item should have ID of id2");
    }

    @Test
    void testUpdate_DecrementSectionOrder() {
        // Arrange
        CreateColumnSectionRequest request1 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        long id1 = columnSectionService.create(request1);

        CreateColumnSectionRequest request2 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(2)
                .build();

        long id2 = columnSectionService.create(request2);
        ColumnSectionResponse columnSection = columnSectionService.get(id2);

        CreateColumnSectionRequest request3 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(3)
                .build();

        Long id3 = columnSectionService.create(request3);

        // Act
        columnSectionService.update(UpdateColumnSectionRequest.builder()
                .id(id2)
                .latexMethodId(columnSection.getLatexMethod().getId())
                .itemOrder(3)
                .endsep(2.0)
                .itemsep(2.0)
                .build());

        // Assert
        List<ColumnSection> items = columnSectionRepository.findAllByColumnId(columnId);
        // Sort by section order for easier comparison
        items.sort(Comparator.comparing(ColumnSection::getSectionOrder));

        assertEquals(3, items.size(), "There should be 3 items");
        assertEquals(List.of(1, 2, 3),
                items.stream().map(ColumnSection::getSectionOrder).collect(Collectors.toList()),
                "Item orders should be 1, 2, 3");
        assertEquals(id1, items.get(0).getId(), "First item should have ID of id1");
        assertEquals(id3, items.get(1).getId(), "Second item should have ID of id3");
        assertEquals(id2, items.get(2).getId(), "Third item should have ID of id2");
    }

    @Test
    void testDelete() {
        // Arrange
        CreateColumnSectionRequest request = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        Long columnSectionId = columnSectionService.create(request);

        // Act
        columnSectionService.delete(columnSectionId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.get(columnSectionId),
                "Column section should not be found after deletion");
    }

    @Test
    void testDelete_DecrementSectionOrder() {
        // Arrange
        CreateColumnSectionRequest request1 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        long id1 = columnSectionService.create(request1);

        CreateColumnSectionRequest request2 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(2)
                .build();

        long id2 = columnSectionService.create(request2);

        CreateColumnSectionRequest request3 = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(3)
                .build();

        long id3 = columnSectionService.create(request3);

        // Act
        columnSectionService.delete(id2);

        // Assert
        List<ColumnSection> items = columnSectionRepository.findAllByColumnId(columnId);
        items.sort(Comparator.comparing(ColumnSection::getSectionOrder)); // Sort by section order for easier
                                                                       // comparison

        assertEquals(2, items.size(), "There should be 2 items");
        assertEquals(List.of(1, 2),
                items.stream().map(ColumnSection::getSectionOrder).collect(Collectors.toList()),
                "Section orders should be 1, 2");
        assertEquals(id1, items.get(0).getId(), "First item should have ID of id1");
        assertEquals(id3, items.get(1).getId(), "Second item should have ID of id3");
    }

    @Test
    void testNullChecks() {
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> columnSectionService.create(null),
                "Should throw ConstraintViolationException for null request on create");
        assertThrows(ConstraintViolationException.class, () -> columnSectionService.get(null),
                "Should throw ConstraintViolationException for null ID on get");
        assertThrows(ConstraintViolationException.class, () -> columnSectionService.update(null),
                "Should throw ConstraintViolationException for null request on update");
        assertThrows(ConstraintViolationException.class, () -> columnSectionService.delete(null),
                "Should throw ConstraintViolationException for null ID on delete");
    }

    @Test
    void testAccessDenied() {
        // Arrange
        CreateColumnSectionRequest request = CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .itemOrder(1)
                .build();

        Long columnSectionId = columnSectionService.create(request);

        // Set the Authentication object to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> columnSectionService.create(request),
                "Should throw AccessDeniedException for unauthorized access to create");
        assertThrows(AccessDeniedException.class, () -> columnSectionService.get(columnSectionId),
                "Should throw AccessDeniedException for unauthorized access to get");
        assertThrows(AccessDeniedException.class,
                () -> columnSectionService.update(UpdateColumnSectionRequest.builder()
                        .id(columnSectionId).itemOrder(2).build()),
                "Should throw AccessDeniedException for unauthorized access to update");
        assertThrows(AccessDeniedException.class, () -> columnSectionService.delete(columnSectionId),
                "Should throw AccessDeniedException for unauthorized access to delete");
    }

    @Test
    void testEntityNotFound() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.get(999L),
                "Should throw EntityNotFoundException for non-existent column section on get");
        assertThrows(EntityNotFoundException.class,
                () -> columnSectionService.update(
                        UpdateColumnSectionRequest.builder().id(999L).itemOrder(2).build()),
                "Should throw EntityNotFoundException for non-existent column section on update");
        assertThrows(EntityNotFoundException.class, () -> columnSectionService.delete(999L),
                "Should throw EntityNotFoundException for non-existent column section on delete");
    }
}