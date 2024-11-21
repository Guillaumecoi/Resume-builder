package com.coigniez.resumebuilder.model.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ResumeMapperTest {

    @Autowired
    private ResumeMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        Resume entity = Resume.builder()
            .id(1L)
            .title("Software Engineer")
            .firstName("John")
            .lastName("Doe")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
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