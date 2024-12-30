package com.coigniez.resumebuilder.domain.column;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.column.dtos.UpdateColumnRequest;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
public class ColumnMapperTest {

    @Autowired
    private ColumnMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        Column entity = Column.builder()
                .id(1L)
                .columnNumber(1)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0)
                .paddingRight(10.0)
                .paddingTop(20.0)
                .paddingBottom(20.0)
                .borderLeft(0.0)
                .borderRight(0.0)
                .borderTop(0.0)
                .borderBottom(0.0)
                .build();

        // Act
        ColumnResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getColumnNumber(), dto.getColumnNumber());
        assertEquals(entity.getBackgroundColor(), dto.getBackgroundColor());
        assertEquals(entity.getTextColor(), dto.getTextColor());
        assertEquals(entity.getBorderColor(), dto.getBorderColor());
        assertEquals(entity.getPaddingLeft(), dto.getPaddingLeft());
        assertEquals(entity.getPaddingRight(), dto.getPaddingRight());
        assertEquals(entity.getPaddingTop(), dto.getPaddingTop());
        assertEquals(entity.getPaddingBottom(), dto.getPaddingBottom());
        assertEquals(entity.getBorderLeft(), dto.getBorderLeft());
        assertEquals(entity.getBorderRight(), dto.getBorderRight());
        assertEquals(entity.getBorderTop(), dto.getBorderTop());
        assertEquals(entity.getBorderBottom(), dto.getBorderBottom());
    }

    @Test
    void testToEntity() {
        // Arrange
        CreateColumnRequest request = CreateColumnRequest.builder()
                .layoutId(1L)
                .columnNumber(1)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0)
                .paddingRight(10.0)
                .paddingTop(20.0)
                .paddingBottom(20.0)
                .borderLeft(0.0)
                .borderRight(0.0)
                .borderTop(0.0)
                .borderBottom(0.0)
                .build();

        // Act
        Column entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getColumnNumber(), entity.getColumnNumber());
        assertEquals(request.getBackgroundColor(), entity.getBackgroundColor());
        assertEquals(request.getTextColor(), entity.getTextColor());
        assertEquals(request.getBorderColor(), entity.getBorderColor());
        assertEquals(request.getPaddingLeft(), entity.getPaddingLeft());
        assertEquals(request.getPaddingRight(), entity.getPaddingRight());
        assertEquals(request.getPaddingTop(), entity.getPaddingTop());
        assertEquals(request.getPaddingBottom(), entity.getPaddingBottom());
        assertEquals(request.getBorderLeft(), entity.getBorderLeft());
        assertEquals(request.getBorderRight(), entity.getBorderRight());
        assertEquals(request.getBorderTop(), entity.getBorderTop());
        assertEquals(request.getBorderBottom(), entity.getBorderBottom());
    }

    @Test
    void testToEntity_DefaultValues() {
        // Arrange
        CreateColumnRequest request = CreateColumnRequest.builder()
                .layoutId(1L)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .build();

        // Act
        Column entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(1, entity.getColumnNumber());
        assertEquals(10.0, entity.getPaddingLeft());
        assertEquals(10.0, entity.getPaddingRight());
        assertEquals(20.0, entity.getPaddingTop());
        assertEquals(20.0, entity.getPaddingBottom());
        assertEquals(0.0, entity.getBorderLeft());
        assertEquals(0.0, entity.getBorderRight());
        assertEquals(0.0, entity.getBorderTop());
        assertEquals(0.0, entity.getBorderBottom());
    }

    @Test
    void testToEntity_InvalidRequest() {
        // Arrange
        CreateColumnRequest request = CreateColumnRequest.builder().build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(request));
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        UpdateColumnRequest request = UpdateColumnRequest.builder()
                .id(1L)
                .columnNumber(2)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(15.0)
                .paddingRight(15.0)
                .paddingTop(25.0)
                .paddingBottom(25.0)
                .borderLeft(1.0)
                .borderRight(1.0)
                .borderTop(1.0)
                .borderBottom(1.0)
                .build();

        Column entity = Column.builder()
                .id(1L)
                .columnNumber(1)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0)
                .paddingRight(10.0)
                .paddingTop(20.0)
                .paddingBottom(20.0)
                .borderLeft(0.0)
                .borderRight(0.0)
                .borderTop(0.0)
                .borderBottom(0.0)
                .build();

        // Act
        mapper.updateEntity(entity, request);

        // Assert
        assertEquals(1L, entity.getId(), "ID should not be updated");
        assertEquals(2, entity.getColumnNumber(), "Column number should be updated");
        assertEquals(request.getBackgroundColor(), entity.getBackgroundColor());
        assertEquals(request.getTextColor(), entity.getTextColor());
        assertEquals(request.getBorderColor(), entity.getBorderColor());
        assertEquals(request.getPaddingLeft(), entity.getPaddingLeft());
        assertEquals(request.getPaddingRight(), entity.getPaddingRight());
        assertEquals(request.getPaddingTop(), entity.getPaddingTop());
        assertEquals(request.getPaddingBottom(), entity.getPaddingBottom());
        assertEquals(request.getBorderLeft(), entity.getBorderLeft());
        assertEquals(request.getBorderRight(), entity.getBorderRight());
        assertEquals(request.getBorderTop(), entity.getBorderTop());
        assertEquals(request.getBorderBottom(), entity.getBorderBottom());
    }
}