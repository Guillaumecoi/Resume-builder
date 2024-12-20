package com.coigniez.resumebuilder.domain.sectionitem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.annotation.Validated;

import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.*;

import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@Validated
class SectionItemMapperTest {

    @Autowired
    private SectionItemMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        SectionItem entity = SectionItem.builder()
                .id(1L)
                .itemOrder(1)
                .item(Textbox.builder()
                    .content("This is some example text")
                    .build())
                .build();

        // Act
        SectionItemResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getItemOrder(), dto.getItemOrder());
        assertNotNull(dto.getItem());
        assertEquals("This is some example text", ((Textbox) dto.getItem()).getContent());
    }

    @Test
    void testToDto_nullEntity() {
        // Act
        SectionItemResponse dto = mapper.toDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Arrange
        SectionItemRequest request = SectionItemRequest.builder()
            .itemOrder(2)
            .sectionId(1)
            .latexMethodId(1)
            .item(Textbox.builder()
                .content("This is some example text")
                .build())
            .build();

        // Act
        SectionItem entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getItemOrder(), entity.getItemOrder());
        assertTrue(entity.getItem() instanceof Textbox);
        assertEquals("This is some example text", ((Textbox) entity.getItem()).getContent());

    }

    @Test
    void testToEntity_nullRequest() {
        // Act: Map a null request to an entity
        SectionItem entity = mapper.toEntity(null);

        // Assert: Verify the mapping
        assertNull(entity);
    }

    @Test
    void testToEntity_missingRequiredFields() {
        SectionItemRequest dto = SectionItemRequest.builder()
                .itemOrder(1)
                .build();
    
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(dto));
    }

    @Test
    void testToEntity_Skill() {
        // Arrange
        SectionItemRequest correctComplete = SectionItemRequest.builder()
            .itemOrder(1)
            .sectionId(1)
            .latexMethodId(1)
            .item(Skill.builder()
                .name("Java")
                .proficiency(8)
                .build())
            .build();

        SectionItemRequest nullProficiency = SectionItemRequest.builder()
            .itemOrder(1)
            .sectionId(1)
            .latexMethodId(1)
            .item(Skill.builder()
                .name("Java")
                .build())
            .build();

        SectionItemRequest nullName = SectionItemRequest.builder()
            .itemOrder(1)
            .sectionId(1)
            .latexMethodId(1)
            .item(Skill.builder()
                .proficiency(8)
                .build())
            .build();

        SectionItemRequest emptyName = SectionItemRequest.builder()
            .itemOrder(1)
            .sectionId(1)
            .latexMethodId(1)
            .item(Skill.builder()
                .name("")
                .proficiency(8)
                .build())
            .build();

        SectionItemRequest lowProficiency = SectionItemRequest.builder()
            .itemOrder(1)
            .sectionId(1)
            .latexMethodId(1)
            .item(Skill.builder()
                .name("Java")
                .proficiency(0)
                .build())
            .build();

        SectionItemRequest highProficiency = SectionItemRequest.builder()
            .itemOrder(1)
            .sectionId(1)
            .latexMethodId(1)
            .item(Skill.builder()
                .name("Java")
                .proficiency(11)
                .build())
            .build();
    
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(correctComplete);
        SectionItem entityNullProficiency = mapper.toEntity(nullProficiency);
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals("Java", ((Skill) entityCorrectComplete.getItem()).getName());
        assertEquals(8, ((Skill) entityCorrectComplete.getItem()).getProficiency());     
        assertNotNull(entityNullProficiency);
        assertEquals("Java", ((Skill) entityNullProficiency.getItem()).getName());
        assertNull(((Skill) entityNullProficiency.getItem()).getProficiency());
        
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(nullName));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(emptyName));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(lowProficiency));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(highProficiency));
    }

    @Test
    void testToEntity_Textbox() {
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(
                SectionItemRequest.builder()
                        .itemOrder(1)
                        .sectionId(1)
                        .latexMethodId(1)
                        .item(Textbox.builder()
                                .content("This is some example text")
                                .build())
                        .build());
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals("This is some example text", ((Textbox) entityCorrectComplete.getItem()).getContent());
    
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .itemOrder(1)
                        .sectionId(1)
                        .latexMethodId(1)
                        .item(Textbox.builder()
                                .build())
                        .build()));
        
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .itemOrder(1)
                        .item(Textbox.builder()
                                .content("")
                                .build())
                        .build()));
    }

    @Test
    void testToEntity_Education() {
        // Arrange
        Education correctComplete = Education.builder()
            .degree("Bachelor of Science")
            .institution("University of Example")
            .period("2020-2023")
            .description("This is a description")
            .build();
        
        Education nullDegree = Education.builder()
            .institution("University of Example")
            .period("2020-2023")
            .description("This is a description")
            .build();
        
        Education nullInstitution = Education.builder()
            .degree("Bachelor of Science")
            .period("2020-2023")
            .description("This is a description")
            .build();
        
        Education nullDescription = Education.builder()
            .degree("Bachelor of Science")
            .institution("University of Example")
            .period("2020-2023")
            .build();
        
        Education emptyDegree = Education.builder()
            .degree("")
            .institution("University of Example")
            .period("2020-2023")
            .description("This is a description")
            .build();
        
        Education emptyInstitution = Education.builder()
            .degree("Bachelor of Science")
            .institution("")
            .period("2020-2023")
            .description("This is a description")
            .build();
        
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(
            SectionItemRequest.builder()
                .itemOrder(1)
                .sectionId(1)
                .latexMethodId(1)
                .item(correctComplete)
                .build());
        
        SectionItem entityNullDescription = mapper.toEntity(
            SectionItemRequest.builder()
                .itemOrder(1)
                .sectionId(1)
                .latexMethodId(1)
                .item(nullDescription)
                .build());
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals("Bachelor of Science", ((Education) entityCorrectComplete.getItem()).getDegree());
        assertEquals("University of Example", ((Education) entityCorrectComplete.getItem()).getInstitution());
        assertEquals("2020-2023", ((Education) entityCorrectComplete.getItem()).getPeriod());
        assertEquals("This is a description", ((Education) entityCorrectComplete.getItem()).getDescription());
        assertNotNull(entityNullDescription);
    
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .itemOrder(1)
                        .sectionId(1)
                        .latexMethodId(1)
                        .item(nullDegree)
                        .build()));
        
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .itemOrder(1)
                        .sectionId(1)
                        .latexMethodId(1)
                        .item(nullInstitution)
                        .build()));
        
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .itemOrder(1)
                        .sectionId(1)
                        .latexMethodId(1)
                        .item(emptyDegree)
                        .build()));

        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .itemOrder(1)
                        .sectionId(1)
                        .latexMethodId(1)
                        .item(emptyInstitution)
                        .build()));
    }

    @Test
    void testToEntity_Workexperience() {
        // Arrange
        WorkExperience correctComplete = WorkExperience.builder()
            .jobTitle("Software Engineer")
            .companyName("Tech Corp")
            .period("2020-2023")
            .description("This is a description")
            .responsibilities(List.of("Responsibility 1", "Responsibility 2"))
            .build();
    
        WorkExperience nullJobTitle = WorkExperience.builder()
            .companyName("Tech Corp")
            .period("2020-2023")
            .description("This is a description")
            .responsibilities(List.of("Responsibility 1", "Responsibility 2"))
            .build();
    
    
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(
            SectionItemRequest.builder()
                .itemOrder(1)
                .sectionId(1)
                .latexMethodId(1)
                .item(correctComplete)
                .build());
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals("Software Engineer", ((WorkExperience) entityCorrectComplete.getItem()).getJobTitle());
        assertEquals("Tech Corp", ((WorkExperience) entityCorrectComplete.getItem()).getCompanyName());
        assertEquals("2020-2023", ((WorkExperience) entityCorrectComplete.getItem()).getPeriod());
        assertEquals("This is a description", ((WorkExperience) entityCorrectComplete.getItem()).getDescription());
        assertEquals(List.of("Responsibility 1", "Responsibility 2"), ((WorkExperience) entityCorrectComplete.getItem()).getResponsibilities());
        assertEquals("\\item Responsibility 1\n\\item Responsibility 2", ((WorkExperience) entityCorrectComplete.getItem()).getResponsibilitiesAsItems());
    
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
            SectionItemRequest.builder()
                .itemOrder(1)
                .item(nullJobTitle)
                .build()));
    }

    @Test
    void testToEntity_Picture() {
        // Arrange
        Picture correctComplete = Picture.builder()
            .path("path/to/image.jpg")
            .caption("Test Caption")
            .width(0.8)
            .height(1.0)
            .rounded(50)
            .zoom(1.2)
            .xoffset(0.5)
            .yoffset(-0.5)
            .shadow(2.0)
            .build();
    
        Picture nullPath = Picture.builder()
            .caption("Test Caption")
            .width(0.8)
            .height(1.0)
            .rounded(50)
            .zoom(1.2)
            .xoffset(0.5)
            .yoffset(-0.5)
            .shadow(2.0)
            .build();
    
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(
            SectionItemRequest.builder()
                .itemOrder(1)
                .sectionId(1)
                .latexMethodId(1)
                .item(correctComplete)
                .build());
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertTrue(entityCorrectComplete.getItem() instanceof Picture);
        Picture picture = (Picture) entityCorrectComplete.getItem();
        
        assertEquals("path/to/image.jpg", picture.getPath());
        assertEquals("Test Caption", picture.getCaption());
        assertEquals(0.8, picture.getWidth());
        assertEquals(1.0, picture.getHeight());
        assertEquals(50, picture.getRounded());
        assertEquals(1.2, picture.getZoom());
        assertEquals(0.5, picture.getXoffset());
        assertEquals(-0.5, picture.getYoffset());
        assertEquals(2.0, picture.getShadow());
    
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
            SectionItemRequest.builder()
                .itemOrder(1)
                .sectionId(1)
                .latexMethodId(1)
                .item(nullPath)
                .build()));
    }

    @Test
    void testToEntityAndBack() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        SectionItemRequest request = SectionItemRequest.builder()
                .id(1L)
                .itemOrder(2)
                .sectionId(1)
                .latexMethodId(1)
                .item(Textbox.builder()
                    .content("This is some example text")
                    .build())
                .build();

        // Act
        SectionItem entity = mapper.toEntity(request);
        SectionItemResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getItemOrder(), dto.getItemOrder());
        assertNotNull(dto.getItem());
        assertEquals("This is some example text", ((Textbox) dto.getItem()).getContent());
    }

}
