package com.coigniez.resumebuilder.model.sectionitem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.*;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
class SectionItemMapperTest {

    @Autowired
    private SectionItemMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        SectionItemData data = Textbox.builder()
                .content("This is some example text")
                .build();


        SectionItem entity = SectionItem.builder()
                .id(1L)
                .type(SectionItemType.TEXTBOX)
                .itemOrder(1)
                .data(data)
                .build();

        // Ac
        SectionItemResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getType().name(), dto.getType());
        assertEquals(entity.getItemOrder(), dto.getItemOrder());
        assertNotNull(dto.getData());
        assertEquals("This is some example text", ((Map<String, Object>) dto.getData()).get("content"));
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
            .type(SectionItemType.TEXTBOX.name())
            .itemOrder(2)
            .data(new HashMap<>() {{
                put("content", "This is some example text");
            }})
            .build();

        // Act
        SectionItem entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(SectionItemType.TEXTBOX, entity.getType());
        assertEquals(request.getItemOrder(), entity.getItemOrder());
        assertTrue(entity.getData() instanceof Textbox);
        assertEquals("This is some example text", ((Textbox) entity.getData()).getContent());

    }

    @Test
    void testToEntity_nullRequest() {
        // Act: Map a null request to an entity
        SectionItem entity = mapper.toEntity(null);

        // Assert: Verify the mapping
        assertNull(entity);
    }

    @Test
    void testToEntity_Skill() {
        // Arrange
        SectionItemRequest correctComplete = SectionItemRequest.builder()
            .type(SectionItemType.SKILL.name())
            .itemOrder(1)
            .data(new HashMap<>() {{
                put("name", "Java");
                put("proficiency", 8);
            }})
            .build();

        SectionItemRequest nullProficiency = SectionItemRequest.builder()
            .type(SectionItemType.SKILL.name())
            .itemOrder(1)
            .data(new HashMap<>() {{
                put("name", "Java");
            }})
            .build();

        SectionItemRequest nullName = SectionItemRequest.builder()
            .type(SectionItemType.SKILL.name())
            .itemOrder(1)
            .data(new HashMap<>() {{
                put("proficiency", 8);
            }})
            .build();

        SectionItemRequest emptyName = SectionItemRequest.builder()
            .type(SectionItemType.SKILL.name())
            .itemOrder(1)
            .data(new HashMap<>() {{
                put("name", "");
                put("proficiency", 8);
            }})
            .build();

        SectionItemRequest lowProficiency = SectionItemRequest.builder()
            .type(SectionItemType.SKILL.name())
            .itemOrder(1)
            .data(new HashMap<>() {{
                put("name", "Java");
                put("proficiency", 0);
            }})
            .build();

        SectionItemRequest highProficiency = SectionItemRequest.builder()
            .type(SectionItemType.SKILL.name())
            .itemOrder(1)
            .data(new HashMap<>() {{
                put("name", "Java");
                put("proficiency", 11);
            }})
            .build();
    
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(correctComplete);
    
        SectionItem entityNullProficiency = mapper.toEntity(nullProficiency);
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals(SectionItemType.SKILL, entityCorrectComplete.getType());
        assertEquals("Java", ((Skill) entityCorrectComplete.getData()).getName());
        assertEquals(8, ((Skill) entityCorrectComplete.getData()).getProficiency());     
        assertNotNull(entityNullProficiency);
        assertEquals(SectionItemType.SKILL, entityNullProficiency.getType());
        assertEquals("Java", ((Skill) entityNullProficiency.getData()).getName());
        assertNull(((Skill) entityNullProficiency.getData()).getProficiency());
        
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(nullName));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(emptyName));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(lowProficiency));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(highProficiency));
    }

    @Test
    void testToEntity_Textbox() {
        // Arrange
        Map<String, Object> correctComplete = new HashMap<>();
        correctComplete.put("content", "This is some example text");
    
        Map<String, Object> nullContent = new HashMap<>();
    
        Map<String, Object> emptyContent = new HashMap<>();
        emptyContent.put("content", "");
    
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.TEXTBOX.name())
                        .itemOrder(1)
                        .data(correctComplete)
                        .build());
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals(SectionItemType.TEXTBOX, entityCorrectComplete.getType());
        assertEquals("This is some example text", ((Textbox) entityCorrectComplete.getData()).getContent());
    
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.TEXTBOX.name())
                        .itemOrder(1)
                        .data(nullContent)
                        .build()));
        
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.TEXTBOX.name())
                        .itemOrder(1)
                        .data(emptyContent)
                        .build()));
    }

    @Test
    void testToEntity_Education() {
        // Arrange
        Map<String, Object> correctComplete = new HashMap<>();
        correctComplete.put("degree", "Bachelor of Science");
        correctComplete.put("institution", "University of Example");
        correctComplete.put("period", "2020-2023");
        correctComplete.put("description", "This is a description");
    
        Map<String, Object> nullDegree = new HashMap<>(correctComplete);
        nullDegree.remove("degree");
    
        Map<String, Object> nullInstitution = new HashMap<>(correctComplete);
        nullInstitution.remove("institution");
    
        Map<String, Object> nullDescription = new HashMap<>(correctComplete);
        nullDescription.remove("description");
    
        Map<String, Object> emptyDegree = new HashMap<>(correctComplete);
        emptyDegree.put("degree", "");
    
        Map<String, Object> emptyInstitution = new HashMap<>(correctComplete);
        emptyInstitution.put("institution", "");
    
        Map<String, Object> lateStartDate = new HashMap<>(correctComplete);
        lateStartDate.put("startDate", LocalDate.of(2024, 1, 1));
    
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.EDUCATION.name())
                        .itemOrder(1)
                        .data(correctComplete)
                        .build());
    
        SectionItem entityNullDescription = mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.EDUCATION.name())
                        .itemOrder(1)
                        .data(nullDescription)
                        .build());
    
        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals(SectionItemType.EDUCATION, entityCorrectComplete.getType());
        assertEquals("Bachelor of Science", ((Education) entityCorrectComplete.getData()).getDegree());
        assertEquals("University of Example", ((Education) entityCorrectComplete.getData()).getInstitution());
        assertEquals("2020-2023", ((Education) entityCorrectComplete.getData()).getPeriod());
        assertEquals("This is a description", ((Education) entityCorrectComplete.getData()).getDescription());
        assertNotNull(entityNullDescription);
    
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.EDUCATION.name())
                        .itemOrder(1)
                        .data(nullDegree)
                        .build()));
        
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.EDUCATION.name())
                        .itemOrder(1)
                        .data(nullInstitution)
                        .build()));
        
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.EDUCATION.name())
                        .itemOrder(1)
                        .data(emptyDegree)
                        .build()));
    }

    @Test
    void testToEntity_Workexperience() {
        // Arrange
        Map<String, Object> correctComplete = new HashMap<>();
        correctComplete.put("jobTitle", "Software Engineer");
        correctComplete.put("companyName", "Tech Corp");
        correctComplete.put("period", "2020-2023");
        correctComplete.put("description", "This is a description");
        correctComplete.put("responsibilities", "Responsibility 1\nResponsibility 2");

        Map<String, Object> nullJobTitle = new HashMap<>(correctComplete);
        nullJobTitle.remove("jobTitle");

        Map<String, Object> lateStartDate = new HashMap<>(correctComplete);
        lateStartDate.put("startDate", LocalDate.of(2024, 1, 1));

        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.WORK_EXPERIENCE.name())
                        .itemOrder(1)
                        .data(correctComplete)
                        .build());

        // Assert
        assertNotNull(entityCorrectComplete);
        assertEquals(SectionItemType.WORK_EXPERIENCE, entityCorrectComplete.getType());
        assertEquals("Software Engineer", ((WorkExperience) entityCorrectComplete.getData()).getJobTitle());
        assertEquals("Tech Corp", ((WorkExperience) entityCorrectComplete.getData()).getCompanyName());
        assertEquals("2020-2023", ((WorkExperience) entityCorrectComplete.getData()).getPeriod());
        assertEquals("This is a description", ((WorkExperience) entityCorrectComplete.getData()).getDescription());
        assertEquals("Responsibility 1\nResponsibility 2", ((WorkExperience) entityCorrectComplete.getData()).getResponsibilities());
        assertEquals(2, ((WorkExperience) entityCorrectComplete.getData()).getResponsibilitiesAsList().size());
        assertEquals("Responsibility 1", ((WorkExperience) entityCorrectComplete.getData()).getResponsibilitiesAsList().get(0));
        assertEquals("Responsibility 2", ((WorkExperience) entityCorrectComplete.getData()).getResponsibilitiesAsList().get(1));

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(
                SectionItemRequest.builder()
                        .type(SectionItemType.WORK_EXPERIENCE.name())
                        .itemOrder(1)
                        .data(nullJobTitle)
                        .build()));
    }

    @Test
    void testToEntity_Picture() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("path", "path/to/image.jpg");
        data.put("caption", "Test Caption");
        data.put("center", true);
        data.put("width", 0.8);
        data.put("height", 1.0);
        data.put("rounded", 50);
        data.put("zoom", 1.2);
        data.put("xoffset", 0.5);
        data.put("yoffset", -0.5);
        data.put("shadow", 2.0);
    
        SectionItemRequest request = SectionItemRequest.builder()
                .type(SectionItemType.PICTURE.name())
                .itemOrder(1)
                .data(data)
                .build();
    
        // Act
        SectionItem entity = mapper.toEntity(request);
    
        // Assert
        assertNotNull(entity);
        assertTrue(entity.getData() instanceof Picture);
        Picture picture = (Picture) entity.getData();
        
        assertEquals("path/to/image.jpg", picture.getPath());
        assertEquals("Test Caption", picture.getCaption());
        assertTrue(picture.isCenter());
        assertEquals(0.8, picture.getWidth());
        assertEquals(1.0, picture.getHeight());
        assertEquals(50, picture.getRounded());
        assertEquals(1.2, picture.getZoom());
        assertEquals(0.5, picture.getXoffset());
        assertEquals(-0.5, picture.getYoffset());
        assertEquals(2.0, picture.getShadow());
    }

    @Test
    void testToEntityAndBack() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        SectionItemRequest request = SectionItemRequest.builder()
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(data)
                .build();

        // Act
        SectionItem entity = mapper.toEntity(request);
        SectionItemResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getType().name(), dto.getType());
        assertEquals(entity.getItemOrder(), dto.getItemOrder());
        assertNotNull(dto.getData());
        assertEquals("This is some example text", ((Map<String, Object>) dto.getData()).get("content"));
    }

}
