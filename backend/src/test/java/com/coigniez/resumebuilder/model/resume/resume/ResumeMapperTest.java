package com.coigniez.resumebuilder.model.resume.resume;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.model.resume.personaldetails.PersonalDetails;
import com.coigniez.resumebuilder.model.resume.personaldetails.PersonalDetailsDto;

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
            .createdBy("admin")
            .createdDate(LocalDateTime.parse("2023-01-01T00:00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-01-02T00:00:00"))
            .build();

        // Act
        ResumeDto dto = mapper.toDto(entity);

        // Assert
        assertEquals(dto.getId(), 1L);
        assertEquals(dto.getTitle(), "Software Engineer");
        assertEquals(dto.getPersonalDetails().getFirstName(), "John");
        assertEquals(dto.getPersonalDetails().getLastName(), "Doe");
        assertEquals(dto.getPersonalDetails().getEmail(), "john.doe@example.com");
        assertEquals(dto.getCreatedBy(), "admin");
        assertEquals(dto.getCreatedDate(), "2023-01-01T00:00:00");
        assertEquals(dto.getLastModifiedDate(), "2023-01-02T00:00:00");
    }

    @Test
    void testToDtoWithNullPersonalDetails() {
        // Arrange
        Resume entity = Resume.builder()
            .id(2L)
            .title("Data Scientist")
            .personalDetails(null)
            .createdBy("user")
            .createdDate(LocalDateTime.parse("2023-02-01T00:00:00"))
            .lastModifiedDate(LocalDateTime.parse("2023-02-02T00:00:00"))
            .build();

        // Act
        ResumeDto dto = mapper.toDto(entity);

        // Assert
        assertEquals(dto.getId(), 2L);
        assertEquals(dto.getTitle(), "Data Scientist");
        assertEquals(dto.getPersonalDetails(), null);
        assertEquals(dto.getCreatedBy(), "user");
        assertEquals(dto.getCreatedDate(), "2023-02-01T00:00:00");
        assertEquals(dto.getLastModifiedDate(), "2023-02-02T00:00:00");
    }

    @Test
    void testToEntity() {
        // Arrange
        ResumeDto dto = ResumeDto.builder()
            .id(1L)
            .title("Software Engineer")
            .personalDetails(PersonalDetailsDto.builder()
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
            .createdBy("admin")
            .createdDate("2023-01-01T00:00:00")
            .lastModifiedDate("2023-01-02T00:00:00")
            .build();

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals(entity.getId(), 1L);
        assertEquals(entity.getTitle(), "Software Engineer");
        assertEquals(entity.getPersonalDetails().getFirstName(), "John");
        assertEquals(entity.getPersonalDetails().getLastName(), "Doe");
        assertEquals(entity.getPersonalDetails().getEmail(), "john.doe@example.com");
        assertEquals(entity.getCreatedBy(), "admin");
        assertEquals(entity.getCreatedDate(), LocalDateTime.parse("2023-01-01T00:00:00"));
        assertEquals(entity.getLastModifiedDate(), LocalDateTime.parse("2023-01-02T00:00:00"));
    }

    @Test
    void testToEntityWithNullPersonalDetails() {
        // Arrange
        ResumeDto dto = ResumeDto.builder()
            .id(2L)
            .title("Data Scientist")
            .personalDetails(null)
            .createdBy("user")
            .createdDate("2023-02-01T00:00:00")
            .lastModifiedDate("2023-02-02T00:00:00")
            .build();

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals(entity.getId(), 2L);
        assertEquals(entity.getTitle(), "Data Scientist");
        assertEquals(entity.getPersonalDetails(), null);
        assertEquals(entity.getCreatedBy(), "user");
        assertEquals(entity.getCreatedDate(), LocalDateTime.parse("2023-02-01T00:00:00"));
        assertEquals(entity.getLastModifiedDate(), LocalDateTime.parse("2023-02-02T00:00:00"));
    }
}
