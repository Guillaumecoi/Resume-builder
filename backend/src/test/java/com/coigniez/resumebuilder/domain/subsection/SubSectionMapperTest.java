package com.coigniez.resumebuilder.domain.subsection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResp;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionUpdateReq;

@SpringBootTest
@ActiveProfiles("test")
public class SubSectionMapperTest {

    @Autowired
    private SubSectionMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        SubSection entity = SubSection.builder()
                .id(1L)
                .title("Education")
                .icon("school")
                .showTitle(false)
                .build();

        // Act
        SubSectionResp dto = mapper.toDto(entity);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Education", dto.getTitle());
        assertEquals("school", dto.getIcon());
        assertEquals(false, dto.isShowTitle());
        assertEquals(Collections.emptyList(), dto.getSectionItems());
    }

    @Test
    void testToDtoNull() {
        // Act
        SubSectionResp dto = mapper.toDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        // Arrange
        SubSectionSimpleCreateReq dto = SubSectionSimpleCreateReq.builder()
                .title("Education")
                .icon("school")
                .showTitle(false)
                .sectionItems(List.of(
                    SectionItemCreateReq.builder()
                        .item(Textbox.builder().content("Bachelor's Degree").build())
                        .build()
                ))
                .build();

        // Act
        SubSection entity = mapper.toEntity(dto);

        // Assert
        assertEquals("Education", entity.getTitle());
        assertEquals("school", entity.getIcon());
        assertEquals(false, entity.isShowTitle());
        assertEquals(1, entity.getItems().size());
        assertEquals("Bachelor's Degree", ((Textbox)entity.getItems().get(0).getItem()).getContent());
    }

    @Test
    void testToEntity_WithOrdering() {
        // Arrange
        SubSectionSimpleCreateReq dto = SubSectionSimpleCreateReq.builder()
                .title("Education")
                .sectionItems(List.of(
                    SectionItemCreateReq.builder()
                        .itemOrder(2)
                        .item(Textbox.builder().content("Master's").build())
                        .build(),
                    SectionItemCreateReq.builder()
                        .itemOrder(null)
                        .item(Textbox.builder().content("PhD").build())
                        .build(),
                    SectionItemCreateReq.builder()
                        .itemOrder(1)
                        .item(Textbox.builder().content("Bachelor's").build())
                        .build()
                ))
                .build();
    
        // Act
        SubSection entity = mapper.toEntity(dto);
    
        // Assert
        assertEquals(3, entity.getItems().size());
        assertEquals("Bachelor's", ((Textbox)entity.getItems().get(0).getItem()).getContent());
        assertEquals("Master's", ((Textbox)entity.getItems().get(1).getItem()).getContent());
        assertEquals("PhD", ((Textbox)entity.getItems().get(2).getItem()).getContent());
        
        assertEquals(1, entity.getItems().get(0).getItemOrder());
        assertEquals(2, entity.getItems().get(1).getItemOrder());
        assertEquals(3, entity.getItems().get(2).getItemOrder());
    }
    
    @Test
    void testToEntity_AllUnordered() {
        // Arrange
        SubSectionSimpleCreateReq dto = SubSectionSimpleCreateReq.builder()
                .title("Education")
                .sectionItems(List.of(
                    SectionItemCreateReq.builder()
                        .item(Textbox.builder().content("First").build())
                        .build(),
                    SectionItemCreateReq.builder()
                        .item(Textbox.builder().content("Second").build())
                        .build()
                ))
                .build();
    
        // Act
        SubSection entity = mapper.toEntity(dto);
    
        // Assert
        assertEquals(2, entity.getItems().size());
        assertEquals("First", ((Textbox)entity.getItems().get(0).getItem()).getContent());
        assertEquals("Second", ((Textbox)entity.getItems().get(1).getItem()).getContent());
        assertEquals(1, entity.getItems().get(0).getItemOrder());
        assertEquals(2, entity.getItems().get(1).getItemOrder());
    }

    @Test
    void testToEntityNull() {
        // Act
        SubSection entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    void testToEntity_DefaultValues() {
        // Arrange
        SubSectionSimpleCreateReq dto = SubSectionSimpleCreateReq.builder()
                .title("Education")
                .build();

        // Act
        SubSection entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("Education", entity.getTitle());
        assertEquals(true, entity.isShowTitle()); // Default value
        assertNull(entity.getIcon());
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        SubSection entity = SubSection.builder()
                .id(1L)
                .title("Education")
                .icon("school")
                .showTitle(true)
                .build();

        SubSectionUpdateReq updateRequest = SubSectionUpdateReq.builder()
                .id(1L)
                .sectionId(1L)
                .title("Work Experience")
                .icon("work")
                .showTitle(false)
                .build();

        // Act
        mapper.updateEntity(entity, updateRequest);

        // Assert
        assertEquals("Work Experience", entity.getTitle());
        assertEquals("work", entity.getIcon());
        assertEquals(false, entity.isShowTitle());
    }

    @Test
    void testUpdateEntityNull() {
        // Arrange
        SubSection entity = SubSection.builder()
                .title("Original")
                .build();

        // Act
        mapper.updateEntity(entity, null);

        // Assert
        assertEquals("Original", entity.getTitle());
    }
}