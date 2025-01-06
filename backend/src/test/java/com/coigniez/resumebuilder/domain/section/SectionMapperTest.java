package com.coigniez.resumebuilder.domain.section;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.domain.section.dtos.SectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionUpdateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionSimpleCreateReq;

import jakarta.validation.ConstraintViolationException;

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
                .icon("school")
                .showTitle(false)
                .build();

        // Act
        SectionResp dto = mapper.toDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Education", dto.getTitle());
        assertEquals("school", dto.getIcon());
        assertEquals(Collections.emptyList(), dto.getSubSections());
    }

    @Test
    void testToEntity() {
        // Arrange
        SectionSimpleCreateReq dto = SectionSimpleCreateReq.builder()
                .title("Education")
                .icon("school")
                .showTitle(false)
                .subSections(List.of(SubSectionSimpleCreateReq.builder().title("Bachelor").build(),
                        SubSectionSimpleCreateReq.builder().title("Master").build()))
                .build();

        // Act
        Section entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Education", entity.getTitle());
        assertEquals("school", entity.getIcon());
        assertEquals(false, entity.isShowTitle());
        assertEquals(2, entity.getSubSections().size());
        assertEquals("Bachelor", entity.getSubSections().get(0).getTitle());
        assertEquals("Master", entity.getSubSections().get(1).getTitle());
    }

    @Test
    void testToEntity_defaultValues() {
        // Arrange
        SectionSimpleCreateReq dto = SectionSimpleCreateReq.builder()
                .title("Education")
                .build();

        // Act
        Section entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Education", entity.getTitle());
        assertEquals(null, entity.getIcon());
        assertEquals(true, entity.isShowTitle());
    }

    @Test
    void testToEntity_ThrowsConstraintViolationException() {
        // Arrange
        SectionSimpleCreateReq dto = SectionSimpleCreateReq.builder()
                .title(null)
                .build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(dto));
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        Section entity = Section.builder()
                .title("Education")
                .icon("school")
                .showTitle(false)
                .build();
        SectionUpdateReq dto = SectionUpdateReq.builder()
                .id(1L)
                .title("Work Experience")
                .icon("work")
                .showTitle(true)
                .build();

        // Act
        mapper.updateEntity(entity, dto);

        // Assert
        assertEquals("Work Experience", entity.getTitle());
        assertEquals("work", entity.getIcon());
        assertEquals(true, entity.isShowTitle());
    }
}