package com.coigniez.resumebuilder.domain.column;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
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
        LayoutColumn entity = LayoutColumn.builder()
                .id(1L)
                .columnNumber((short) 1)
                .ColumnSize(1.0f)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0f)
                .paddingRight(10.0f)
                .paddingTop(20.0f)
                .paddingBottom(20.0f)
                .borderLeft(0.0f)
                .borderRight(0.0f)
                .borderTop(0.0f)
                .borderBottom(0.0f)
                .build();

        // Act
        ColumnResp dto = mapper.toDto(entity);

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
        ColumnCreateReq request = ColumnCreateReq.builder()
                .columnHolderId(1L)
                .columnNumber((short) 1)
                .columnSize(1.0f)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0f)
                .paddingRight(10.0f)
                .paddingTop(20.0f)
                .paddingBottom(20.0f)
                .borderLeft(0.0f)
                .borderRight(0.0f)
                .borderTop(0.0f)
                .borderBottom(0.0f)
                .build();

        // Act
        LayoutColumn entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getColumnNumber(), entity.getColumnNumber());
        assertEquals(request.getColumnSize(), entity.getColumnSize());
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
    void testToEntity_InvalidRequest() {
        // Arrange
        ColumnCreateReq request = ColumnCreateReq.builder().columnNumber((short) -1).build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(request));
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        ColumnUpdateReq request = ColumnUpdateReq.builder()
                .id(1L)
                .columnNumber((short) 2)
                .columnSize(2.0f)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(15.0f)
                .paddingRight(15.0f)
                .paddingTop(25.0f)
                .paddingBottom(25.0f)
                .borderLeft(1.0f)
                .borderRight(1.0f)
                .borderTop(1.0f)
                .borderBottom(1.0f)
                .build();

        LayoutColumn entity = LayoutColumn.builder()
                .id(1L)
                .columnNumber((short) 1)
                .ColumnSize(1.0f)
                .backgroundColor(ColorLocation.PRIMARY)
                .textColor(ColorLocation.SECONDARY)
                .borderColor(ColorLocation.ACCENT)
                .paddingLeft(10.0f)
                .paddingRight(10.0f)
                .paddingTop(20.0f)
                .paddingBottom(20.0f)
                .borderLeft(0.0f)
                .borderRight(0.0f)
                .borderTop(0.0f)
                .borderBottom(0.0f)
                .build();

        // Act
        mapper.updateEntity(entity, request);

        // Assert
        assertEquals(1L, entity.getId(), "ID should not be updated");
        assertEquals(2, entity.getColumnNumber(), "Column number should be updated");
        assertEquals(2.0f, entity.getColumnSize(), "Column size should be updated");
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