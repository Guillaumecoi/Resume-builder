package com.coigniez.resumebuilder.domain.section;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;

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
        SectionResp dto = mapper.toDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Education", dto.getTitle());
    }

    @Test
    void testToEntity() {
        // Arrange
        SectionCreateReq dto = SectionCreateReq.builder()
            .title("Education")
            .build();

        // Act
        Section entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Education", entity.getTitle());
    }
}