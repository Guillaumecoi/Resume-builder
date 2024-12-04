package com.coigniez.resumebuilder.model.section;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionResponse;

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
        SectionRequest dto = SectionRequest.builder()
            .title("Education")
            .build();

        // Act
        Section entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Education", entity.getTitle());
    }
}