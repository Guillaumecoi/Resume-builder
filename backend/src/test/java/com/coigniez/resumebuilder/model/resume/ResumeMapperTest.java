package com.coigniez.resumebuilder.model.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.model.section.Section;

@SpringBootTest
@ActiveProfiles("test")
public class ResumeMapperTest {

    @Autowired
    private ResumeMapper mapper;

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
            .firstName("John")
            .lastName("Doe")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
            .sections(Arrays.asList(section1, section2))
            .build();
    
        // Act
        ResumeResponse dto = mapper.toDto(entity);
    
        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
        assertEquals(2, dto.getSections().size());
        assertEquals("Education", dto.getSections().get(0).getTitle());
        assertEquals("Experience", dto.getSections().get(1).getTitle());
    }

    @Test
    void testToDtoWithNullValues() {
        // Arrange
        Resume entity = Resume.builder()
            .id(1L)
            .title("Software Engineer")
            .firstName("John")
            .lastName("Doe")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
            .sections(null)
            .build();

        // Act
        ResumeResponse dto = mapper.toDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
        assertEquals(Collections.emptyList(), dto.getSections());
    }

    @Test
    void testToEntity() {
        // Arrange
        ResumeRequest dto = new ResumeRequest(
            "Software Engineer",
            "John",
            "Doe"     
        );

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Software Engineer", entity.getTitle());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
    }
}