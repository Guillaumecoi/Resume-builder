package com.coigniez.resumebuilder.domain.columnsection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.dtos.UpdateColumnSectionRequest;

@SpringBootTest
@ActiveProfiles("test")
public class ColumnSectionMapperTest {

    @Autowired
    private ColumnSectionMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        ColumnSection entity = ColumnSection.builder()
                .id(1L)
                .itemOrder(1)
                .itemsep(8.0)
                .endsep(20.0)
                .build();

        // Act
        ColumnSectionResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getSectionOrder(), dto.getSectionOrder());
        assertEquals(entity.getItemsep(), dto.getItemsep());
        assertEquals(entity.getEndsep(), dto.getEndsep());
    }

    @Test
    void testToEntity() {
        // Arrange
        CreateColumnSectionRequest request = CreateColumnSectionRequest.builder()
                .itemOrder(1)
                .itemsep(8.0)
                .endsep(20.0)
                .build();

        // Act
        ColumnSection entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getSectionOrder(), entity.getSectionOrder());
        assertEquals(request.getItemsep(), entity.getItemsep());
        assertEquals(request.getEndsep(), entity.getEndsep());
    }

    @Test
    void testToEntity_DefaultValues() {
        // Arrange
        CreateColumnSectionRequest request = CreateColumnSectionRequest.builder()
                .itemOrder(1)
                .build();

        // Act
        ColumnSection entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(1, entity.getSectionOrder());
        assertEquals(8.0, entity.getItemsep());
        assertEquals(20.0, entity.getEndsep());
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        UpdateColumnSectionRequest request = UpdateColumnSectionRequest.builder()
                .id(2L)
                .itemOrder(2)
                .itemsep(10.0)
                .endsep(25.0)
                .build();

        ColumnSection entity = ColumnSection.builder()
                .id(1L)
                .itemOrder(1)
                .itemsep(8.0)
                .endsep(20.0)
                .build();

        // Act
        mapper.updateEntity(entity, request);

        // Assert
        assertEquals(1L, entity.getId(), "ID should not be updated");
        assertEquals(2, entity.getSectionOrder(), "Section order should be updated");
        assertEquals(request.getItemsep(), entity.getItemsep());
        assertEquals(request.getEndsep(), entity.getEndsep());
    }
}
