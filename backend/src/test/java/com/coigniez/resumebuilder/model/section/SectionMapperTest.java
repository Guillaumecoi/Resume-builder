package com.coigniez.resumebuilder.model.section;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SectionMapperTest {

    @Autowired
    private SectionMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        Section entity = Section.builder()
            .id(1L)
            .title("Education")
            .build();

        // Act
        SectionResponse dto = mapper.toDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Education", dto.getTitle());
    }

    @Test
    void testToEntity() {
        // Arrange
        SectionRequest dto = new SectionRequest("Education");

        // Act
        Section entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Education", entity.getTitle());
    }
}