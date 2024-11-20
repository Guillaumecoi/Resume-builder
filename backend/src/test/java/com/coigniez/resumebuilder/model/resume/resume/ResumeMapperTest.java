package com.coigniez.resumebuilder.model.resume.resume;

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
            .email("john.doe@example.com")
            .phone("+1234567890")
            .address("123 Main St")
            .website("https://example.com")
            .linkedIn("https://linkedin.com/in/johndoe")
            .github("github")
            .instagram("https://instagram.com/johndoe")
            .facebook("https://facebook.com/johndoe")
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
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("+1234567890", dto.getPhone());
        assertEquals("123 Main St", dto.getAddress());
        assertEquals("https://example.com", dto.getWebsite());
        assertEquals("https://linkedin.com/in/johndoe", dto.getLinkedIn());
        assertEquals("github", dto.getGithub());
        assertEquals("https://instagram.com/johndoe", dto.getInstagram());
        assertEquals("https://facebook.com/johndoe", dto.getFacebook());
        assertEquals("2023-01-01T00:00", dto.getCreatedDate());
        assertEquals("2023-01-02T00:00", dto.getLastModifiedDate());
    }

    @Test
    void testToEntity() {
        // Arrange
        ResumeRequest dto = new ResumeRequest(
            "Software Engineer",
            "John",
            "Doe",
            "john.doe@example.com",
            "+1234567890",
            "123 Main St",
            "https://example.com",
            "https://linkedin.com/in/johndoe",
            "github",
            "https://instagram.com/johndoe",
            "https://facebook.com/johndoe"            
        );

        // Act
        Resume entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Software Engineer", entity.getTitle());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("john.doe@example.com", entity.getEmail());
        assertEquals("+1234567890", entity.getPhone());
        assertEquals("123 Main St", entity.getAddress());
        assertEquals("https://example.com", entity.getWebsite());
        assertEquals("https://linkedin.com/in/johndoe", entity.getLinkedIn());
        assertEquals("github", entity.getGithub());
        assertEquals("https://instagram.com/johndoe", entity.getInstagram());
        assertEquals("https://facebook.com/johndoe", entity.getFacebook());
    }
}