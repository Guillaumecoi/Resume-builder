package com.coigniez.resumebuilder.domain.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.UpdateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.templates.ColorTemplates;
import com.coigniez.resumebuilder.templates.LayoutTemplate;

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
                .numberOfColumns(1)
                .columnSeparator(0.35)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .build();

        // Act
        LayoutResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getPageSize(), dto.getPageSize());
        assertEquals(entity.getNumberOfColumns(), dto.getNumberOfColumns());
        assertEquals(entity.getColumnSeparator(), dto.getColumnSeparator());
        assertEquals(entity.getColorScheme(), dto.getColorScheme());
    }

    @Test
    void testToEntity() {
        // Arrange
        CreateLayoutRequest request = CreateLayoutRequest.builder()
                .resumeId(1L)
                .pageSize(PageSize.A4)
                .numberOfColumns(1)
                .columnSeparator(0.35)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .build();

        // Act
        Layout entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getPageSize(), entity.getPageSize());
        assertEquals(request.getNumberOfColumns(), entity.getNumberOfColumns());
        assertEquals(request.getColumnSeparator(), entity.getColumnSeparator());
        assertEquals(request.getColorScheme(), entity.getColorScheme());
    }

    @Test
    void testToEntity_DefaultValues() {
        // Arrange
        CreateLayoutRequest request = CreateLayoutRequest.builder()
                .resumeId(1L)
                .build();

        // Act
        Layout entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(PageSize.A4, entity.getPageSize());
        assertEquals(1, entity.getNumberOfColumns());
        assertEquals(0.35, entity.getColumnSeparator());
        assertEquals(ColorTemplates.EXECUTIVE_SUITE.getName(), entity.getColorScheme().getName());
    }

    @Test
    void testToEntity_InvalidRequest() {
        // Arrange
        CreateLayoutRequest request = CreateLayoutRequest.builder().numberOfColumns(-1).build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(request));
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        UpdateLayoutRequest request = UpdateLayoutRequest.builder()
                .id(2L)
                .pageSize(PageSize.A4)
                .numberOfColumns(2)
                .columnSeparator(0.5)
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .latexMethods(LayoutTemplate.getStandardMethods())
                .build();

        Layout entity = Layout.builder()
                .id(1L)
                .pageSize(PageSize.A4)
                .numberOfColumns(1)
                .columnSeparator(0.35)
                .build();

        // Act
        mapper.updateEntity(entity, request);

        // Assert
        assertEquals(1L, entity.getId(), "ID should not be updated");
        assertEquals(request.getPageSize(), entity.getPageSize());
        assertEquals(request.getNumberOfColumns(), entity.getNumberOfColumns());
        assertEquals(request.getColumnSeparator(), entity.getColumnSeparator());
        assertEquals(request.getColorScheme(), entity.getColorScheme());
    }
}