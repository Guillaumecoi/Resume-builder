package com.coigniez.resumebuilder.model.resume.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.model.resume.personaldetails.PersonalDetails;

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
            .personalDetails(PersonalDetails.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .address("123 Main St")
                .website("https://example.com")
                .linkedIn("https://linkedin.com/in/johndoe")
                .github("github")
                .instagram("https://instagram.com/johndoe")
                .facebook("https://facebook.com/johndoe")
                .build())
            .createdDate(LocalDateTime.parse("2023-01-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00"))
            .build();

        // Act
        ResumeResponse dto = mapper.toDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Software Engineer", dto.getTitle());
        assertEquals("John", dto.getPersonalDetails().getFirstName());
        assertEquals("Doe", dto.getPersonalDetails().getLastName());
        assertEquals("john.doe@example.com", dto.getPersonalDetails().getEmail());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
    }

    @Test
    void testToDtoWithNullPersonalDetails() {
        // Arrange
        Resume entity = Resume.builder()
            .id(2L)
            .title("Data Scientist")
            .personalDetails(null)
            .createdDate(LocalDateTime.parse("2023-02-01T00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-02-02T00:00"))
            .build();

        // Act
        ResumeResponse dto = mapper.toDto(entity);

        // Assert
        assertEquals(2L, dto.getId());
        assertEquals("Data Scientist", dto.getTitle());
        assertEquals(null, dto.getPersonalDetails());
        assertEquals("2023-02-01T00:00", dto.getCreatedDate());
        assertEquals("2023-02-02T00:00", dto.getLastModifiedDate());
    }

    @Test
    void testToEntity() {
        // Arrange
        ResumeRequest dto = new ResumeRequest("Software Engineer");

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Software Engineer", entity.getTitle());
    }
}