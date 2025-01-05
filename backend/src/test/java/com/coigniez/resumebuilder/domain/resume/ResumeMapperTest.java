package com.coigniez.resumebuilder.domain.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeSimpleResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeUpdateReq;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.domain.section.dtos.SectionSimpleCreateReq;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
public class ResumeMapperTest {

    @Autowired
    private ResumeMapper mapper;

    @Test
    void testToEntity() {
        // Arrange
        ResumeCreateReq dto = ResumeCreateReq.builder().title("Software Engineer")
                .sections(List.of(
                    SectionSimpleCreateReq.builder().title("Education").build(),
                    SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Software Engineer", entity.getTitle());
        assertEquals(2, entity.getSections().size());

        List<String> sectionTitles = entity.getSections().stream()
                .map(Section::getTitle)
                .toList();

        assertEquals(List.of("Education", "Experience"), sectionTitles);
        assertTrue(entity.getSections().stream().allMatch(section -> section.getResume().equals(entity)));
    }

    @Test
    void testToEntity_NullValues() {
        // Arrange
        ResumeCreateReq dto = ResumeCreateReq.builder().title("Software Engineer").sections(null).build();

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Software Engineer", entity.getTitle());
        assertEquals(Collections.emptyList(), entity.getSections());
    }

    @Test
    void testToEntity_InvalidTitle() {
        // Arrange
        ResumeCreateReq dto = ResumeCreateReq.builder().title("").build();

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(dto));
    }

    @Test
    void testToDto() {
        // Arrange
        Section section1 = Section.builder()
            .id(1L)
            .title("Education")
            .build();
    
        Section section2 = Section.builder()
            .id(2L)
            .title("Experience")
            .build();
    
        Resume entity = Resume.builder()
            .id(1L)
            .title("Software Engineer")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
            .sections(Arrays.asList(section1, section2))
            .build();
    
        // Act
        ResumeResp dto = mapper.toDto(entity);
    
        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
        assertEquals(2, dto.getSections().size());

        List<String> sectionTitles = dto.getSections().stream()
                .map(SectionResp::getTitle)
                .toList();

        assertEquals(List.of("Education", "Experience"), sectionTitles);
    }

    @Test
    void testToDto_NullValues() {
        // Arrange
        Resume entity = Resume.builder()
            .id(1L)
            .title("Software Engineer")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
            .sections(null)
            .build();

        // Act
        ResumeResp dto = mapper.toDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
        assertEquals(Collections.emptyList(), dto.getSections());
    }

    @Test
    void testToSimpleDto() {
        // Arrange
        Resume entity = Resume.builder()
            .id(1L)
            .title("Software Engineer")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
            .build();

        // Act
        ResumeSimpleResp dto = mapper.toSimpleDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        Resume entity = Resume.builder().id(1L).title("Software Engineer").build();
        ResumeUpdateReq dto = ResumeUpdateReq.builder().id(1L).title("Software Developer").build();

        // Act
        mapper.updateEntity(entity, dto);

        // Assert
        assertEquals("Software Developer", entity.getTitle());
    }
}