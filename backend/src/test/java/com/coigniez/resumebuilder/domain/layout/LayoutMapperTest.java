package com.coigniez.resumebuilder.domain.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResp;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.Header;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageUpdateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutUpdateReq;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.templates.color.ColorTemplates;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
public class LayoutMapperTest {

    @Autowired
    private LayoutMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        Layout entity = Layout.builder()
                .id(1L)
                .resume(Resume.builder().build())
                .pageSize(PageSize.A4)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .build();

        // Act
        LayoutResp dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getPageSize(), dto.getPageSize());
        assertEquals(entity.getColorScheme(), dto.getColorScheme());
    }

    @Test
    void testToDto_WithHeaderAndPages() {
        // Arrange
        Header header = Header.builder()
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        List<LayoutPage> pages = List.of(
                LayoutPage.builder().pageNumber(1).build(),
                LayoutPage.builder().pageNumber(2).build());

        Layout entity = Layout.builder()
                .id(1L)
                .resume(Resume.builder().build())
                .pageSize(PageSize.A4)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .header(header)
                .pages(pages)
                .build();

        // Act
        LayoutResp dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getPageSize(), dto.getPageSize());
        assertEquals(entity.getColorScheme(), dto.getColorScheme());

        // Verify header
        assertNotNull(dto.getHeader());
        assertEquals(0.100, dto.getHeader().getHeight());
        assertTrue(dto.getHeader().getRepeatOnEveryPage());

        // Verify pages
        assertNotNull(dto.getPages());
        assertEquals(2, dto.getPages().size());
        assertEquals(1, dto.getPages().get(0).getPageNumber());
        assertEquals(2, dto.getPages().get(1).getPageNumber());
    }

    @Test
    void testToEntity() {
        // Arrange
        LayoutCreateReq request = LayoutCreateReq.builder()
                .resumeId(1L)
                .pageSize(PageSize.A4)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .build();

        // Act
        Layout entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getPageSize(), entity.getPageSize());
        assertEquals(request.getColorScheme(), entity.getColorScheme());
    }

    @Test
    void testToEntity_WithHeaderAndPages() {
        // Arrange
        HeaderCreateReq header = HeaderCreateReq.builder()
                .height(0.100)
                .repeatOnEveryPage(true)
                .build();

        List<PageCreateReq> pages = List.of(
                PageCreateReq.builder().pageNumber(1).build(),
                PageCreateReq.builder().pageNumber(2).build());

        LayoutCreateReq request = LayoutCreateReq.builder()
                .resumeId(1L)
                .pageSize(PageSize.A4)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .header(header)
                .pages(pages)
                .build();

        // Act
        Layout entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getPageSize(), entity.getPageSize());
        assertEquals(request.getColorScheme(), entity.getColorScheme());

        // Verify header
        assertNotNull(entity.getHeader());
        assertEquals(0.100, entity.getHeader().getHeight());
        assertTrue(entity.getHeader().getRepeatOnEveryPage());

        // Verify pages
        assertNotNull(entity.getPages());
        assertEquals(2, entity.getPages().size());
        assertEquals(1, entity.getPages().get(0).getPageNumber());
        assertEquals(2, entity.getPages().get(1).getPageNumber());
    }

    @Test
    void testToEntity_DefaultValues() {
        // Arrange
        LayoutCreateReq request = LayoutCreateReq.builder()
                .resumeId(1L)
                .build();

        // Act
        Layout entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(PageSize.A4, entity.getPageSize());
        assertEquals(ColorTemplates.EXECUTIVE_SUITE.getName(), entity.getColorScheme().getName());
        assertFalse(entity.getLatexMethods().isEmpty());
    }

    @Test
    void testToEntity_InvalidRequest() {
        // Arrange
        LayoutCreateReq request = LayoutCreateReq.builder().build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(request));
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        LayoutUpdateReq request = LayoutUpdateReq.builder()
                .id(2L)
                .pageSize(PageSize.A4)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .build();

        Layout entity = Layout.builder()
                .id(1L)
                .pageSize(PageSize.A4)
                .build();

        // Act
        mapper.updateEntity(entity, request);

        // Assert
        assertEquals(1L, entity.getId(), "ID should not be updated");
        assertEquals(request.getPageSize(), entity.getPageSize());
        assertEquals(request.getColorScheme(), entity.getColorScheme());
    }

    @Test
    void testUpdateEntity_WithPartialHeaderAndPages() {
        // Arrange - Initial entity
        Layout entity = Layout.builder()
                .id(1L)
                .pageSize(PageSize.A4)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .header(Header.builder()
                        .id(4L)
                        .height(0.100)
                        .repeatOnEveryPage(true)
                        .build())
                .pages(List.of(
                        LayoutPage.builder().id(1L).pageNumber(1).build(),
                        LayoutPage.builder().id(2L).pageNumber(2).build(),
                        LayoutPage.builder().id(3L).pageNumber(3).build()))
                .build();

        // Arrange - Update request
        HeaderUpdateReq headerUpdate = HeaderUpdateReq.builder()
                .id(4L)
                .height(0.200)
                .repeatOnEveryPage(false)
                .build();

        List<PageUpdateReq> pageUpdates = List.of(
                PageUpdateReq.builder().id(1L).pageNumber(4).build());

        LayoutUpdateReq request = LayoutUpdateReq.builder()
                .id(1L)
                .pageSize(PageSize.LETTER)
                .colorScheme(ColorTemplates.MODERN)
                .header(headerUpdate)
                .pages(pageUpdates)
                .build();

        // Act
        mapper.updateEntity(entity, request);

        // Assert
        assertEquals(PageSize.LETTER, entity.getPageSize());
        assertEquals(ColorTemplates.MODERN, entity.getColorScheme());

        // Verify header updates
        assertNotNull(entity.getHeader());
        assertEquals(0.200, entity.getHeader().getHeight());
        assertFalse(entity.getHeader().getRepeatOnEveryPage());

        // Verify pages updates
        assertNotNull(entity.getPages());
        assertEquals(3, entity.getPages().size());
        assertEquals(1L, entity.getPages().get(0).getId());
        assertEquals(4, entity.getPages().get(0).getPageNumber());
    }
}