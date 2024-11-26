package com.coigniez.resumebuilder.model.sectionitem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.model.sectionitem.itemtypes.SectionItemType;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
class SectionItemMapperTest {

    @Autowired
    private SectionItemMapper mapper;

    @Test
    void testToDto() {
        // Arrange: Create a SectionItem entity
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

        // Act: Map the entity to a DTO
        SectionItemResponse dto = mapper.toDto(entity);

        // Assert: Verify the mapping
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getType().name(), dto.getType());
        assertEquals(entity.getItemOrder(), dto.getItemOrder());
        assertEquals(entity.getData(), dto.getData());
    }
    @Test
    void testToDto_nullEntity() {
        // Act: Map a null entity to a DTO
        SectionItemResponse dto = mapper.toDto(null);

        // Assert: Verify the mapping
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Arrange: Create a SectionItemRequest
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        SectionItemRequest request = new SectionItemRequest(
                SectionItemType.TEXTBOX.name(),
                2,
                data
        );

        // Act: Map the request to an entity
        SectionItem entity = mapper.toEntity(request);

        // Assert: Verify the mapping
        assertNotNull(entity);
        assertEquals(SectionItemType.TEXTBOX, entity.getType());
        assertEquals(request.itemOrder(), entity.getItemOrder());
        assertEquals(request.data(), entity.getData());
    }

    @Test
    void testToEntity_nullRequest() {
        // Act: Map a null request to an entity
        SectionItem entity = mapper.toEntity(null);

        // Assert: Verify the mapping
        assertNull(entity);
    }
}
