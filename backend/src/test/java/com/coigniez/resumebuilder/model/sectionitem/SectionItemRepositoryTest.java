package com.coigniez.resumebuilder.model.sectionitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRepository;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionItemRepositoryTest {

    @Autowired
    private SectionItemRepository sectionItemRepository;

    @Test
    public void testCreate() {
        // Arrange
        SectionItemData data = Textbox.builder()
                .content("This is some example text")
                .build();


        SectionItem entity = SectionItem.builder()
                .id(null)
                .type(SectionItemType.TEXTBOX)
                .itemOrder(1)
                .data(data)
                .build();

        // Act
        SectionItem savedEntity = sectionItemRepository.save(entity);

        // Assert
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());
        assertEquals(1, savedEntity.getItemOrder());
        assertEquals(SectionItemType.TEXTBOX, savedEntity.getType());
        assertEquals("This is some example text",((Textbox) savedEntity.getData()).getContent());
        
    }
}
