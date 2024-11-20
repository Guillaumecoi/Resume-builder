package com.coigniez.resumebuilder.model.resume.personaldetails;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PersonalDetailsMapperTest {

    private final PersonalDetailsMapper mapper = new PersonalDetailsMapper();

    @Test
    void testToDto() {
        // Arrange
        PersonalDetails entity = PersonalDetails.builder()
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
            .build();

        // Act
        PersonalDetailsDto dto = mapper.toDto(entity);

        // Assert
        assertEquals(dto.getFirstName(), "John");
        assertEquals(dto.getLastName(), "Doe");
        assertEquals(dto.getEmail(), "john.doe@example.com");
        assertEquals(dto.getPhone(), "+1234567890");
        assertEquals(dto.getAddress(), "123 Main St");
        assertEquals(dto.getWebsite(), "https://example.com");
        assertEquals(dto.getLinkedIn(), "https://linkedin.com/in/johndoe");
        assertEquals(dto.getGithub(), "github");
        assertEquals(dto.getInstagram(), "https://instagram.com/johndoe");
        assertEquals(dto.getFacebook(), "https://facebook.com/johndoe");
    }

    @Test
    void testToEntity() {
        // Arrange
        PersonalDetailsDto dto = PersonalDetailsDto.builder()
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
        .build();

        // Act
        PersonalDetails entity = mapper.toEntity(dto);

        // Assert
        assertEquals(entity.getFirstName(), "John");
        assertEquals(entity.getLastName(), "Doe");
        assertEquals(entity.getEmail(), "john.doe@example.com");
        assertEquals(entity.getPhone(), "+1234567890");
        assertEquals(entity.getAddress(), "123 Main St");
        assertEquals(entity.getWebsite(), "https://example.com");
        assertEquals(entity.getLinkedIn(), "https://linkedin.com/in/johndoe");
        assertEquals(entity.getGithub(), "github");
        assertEquals(entity.getInstagram(), "https://instagram.com/johndoe");
        assertEquals(entity.getFacebook(), "https://facebook.com/johndoe");
    }
}