package com.coigniez.resumebuilder.model.sectionitem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.model.sectionitem.itemtypes.*;

import jakarta.validation.ConstraintViolationException;

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
        Map<String, Object> data = new HashMap<>();
        data.put("jobTitle", "Software Engineer");
        data.put("companyName", "Tech Corp");
        data.put("startDate", "2020-01-01");
        data.put("endDate", "2023-01-01");


        SectionItem entity = SectionItem.builder()
                .id(1L)
                .type(SectionItemType.WORK_EXPERIENCE)
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
        assertEquals(entity.getData(), dto.getData());
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
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        SectionItemRequest request = new SectionItemRequest(
                SectionItemType.TEXTBOX.name(),
                2,
                data
        );

        // Act
        SectionItem entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(SectionItemType.TEXTBOX, entity.getType());
        assertEquals(request.itemOrder(), entity.getItemOrder());
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
        Map<String, Object> correctComplete = new HashMap<>();
        correctComplete.put("name", "Java");
        correctComplete.put("proficiency", 8);
    
        Map<String, Object> nullProficiency = new HashMap<>();
        nullProficiency.put("name", "Java");
    
        Map<String, Object> nullName = new HashMap<>();
        nullName.put("proficiency", 8);
    
        Map<String, Object> emptyName = new HashMap<>();
        emptyName.put("name", "");
        emptyName.put("proficiency", 8);
    
        Map<String, Object> lowProficiency = new HashMap<>();
        lowProficiency.put("name", "Java");
        lowProficiency.put("proficiency", 0);
    
        Map<String, Object> highProficiency = new HashMap<>();
        highProficiency.put("name", "Java");
        highProficiency.put("proficiency", 11);
    
        // Act
        SectionItem entityCorrectComplete = mapper.toEntity(new SectionItemRequest(
                SectionItemType.SKILL.name(), 1, correctComplete));
    
        SectionItem entityNullProficiency = mapper.toEntity(new SectionItemRequest(
                SectionItemType.SKILL.name(), 1, nullProficiency));
    
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
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(new SectionItemRequest(
                SectionItemType.SKILL.name(), 1, nullName)));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(new SectionItemRequest(
                SectionItemType.SKILL.name(), 1, emptyName)));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(new SectionItemRequest(
                SectionItemType.SKILL.name(), 1, lowProficiency)));
        assertThrows(ConstraintViolationException.class, () -> mapper.toEntity(new SectionItemRequest(
                SectionItemType.SKILL.name(), 1, highProficiency)));
    }
}
