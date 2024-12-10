package com.coigniez.resumebuilder.domain.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionResponse;

@SpringBootTest
@ActiveProfiles("test")
public class ResumeMapperTest {

    @Autowired
    private ResumeMapper mapper;

    @Test
    void testToEntity() {
        // Arrange
        ResumeRequest dto = ResumeRequest.builder().title("Software Engineer")
                .sections(List.of(
                    SectionRequest.builder().title("Education").build(),
                    SectionRequest.builder().title("Experience").build()))
                .build();

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Software Engineer", entity.getTitle());
        assertEquals(2, entity.getSections().size());

        Set<String> sectionTitles = entity.getSections().stream()
                .map(Section::getTitle)
                .collect(Collectors.toSet());

        assertEquals(Set.of("Education", "Experience"), sectionTitles);
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
            .sections(new HashSet<>(Arrays.asList(section1, section2)))
            .build();
    
        // Act
        ResumeDetailResponse dto = mapper.toDto(entity);
    
        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
        assertEquals(2, dto.getSections().size());

        Set<String> sectionTitles = dto.getSections().stream()
                .map(SectionResponse::getTitle)
                .collect(Collectors.toSet());

        assertEquals(Set.of("Education", "Experience"), sectionTitles);
    }

    @Test
    void testToDtoWithNullValues() {
        // Arrange
        Resume entity = Resume.builder()
            .id(1L)
            .title("Software Engineer")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
            .sections(null)
            .build();

        // Act
        ResumeDetailResponse dto = mapper.toDto(entity);

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
        ResumeResponse dto = mapper.toSimpleDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
    }
}