package com.coigniez.resumebuilder.domain.sectionitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionItemRepositoryTest {

    @Autowired
    private SectionItemRepository sectionItemRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EntityManager entityManager;

    private Section section;

    @BeforeEach
    public void setUp() {
        section = Section.builder().title("Test Section").build();
        sectionRepository.save(section);

        SectionItem item1 = SectionItem.builder()
                .section(section)
                .itemOrder(1)
                .data(Skill.builder().name("Java").build())
                .build();

        SectionItem item2 = SectionItem.builder()
                .section(section)
                .itemOrder(2)
                .data(Skill.builder().name("Python").build())
                .build();

        SectionItem item3 = SectionItem.builder()
                .section(section)
                .itemOrder(3)
                .data(Skill.builder().name("HTML").build())
                .build();

        sectionItemRepository.saveAll(List.of(item1, item2, item3));
    }

    @Test
    public void testFindMaxItemOrderBySectionId() {
        // Act
        int maxOrder = sectionItemRepository.findMaxItemOrderBySectionId(section.getId()).orElse(0);
    
        // Assert
        assertEquals(3, maxOrder, "The maximum item order should be 3");
    }
    
    @Test
    public void testIncrementItemOrderBetween_1difference() {
        // Act
        sectionItemRepository.incrementItemOrderBetween(section.getId(), 2, 3);
        entityManager.clear();
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(section.getId());
        items.sort(Comparator.comparing(SectionItem::getItemOrder));
    
        // Assert
        assertEquals(1, items.get(0).getItemOrder(), "First item should have order 1");
        assertEquals(3, items.get(1).getItemOrder(), "Second item should have order 3");
        assertEquals(3, items.get(2).getItemOrder(), "Third item should have order 3 (unchanged)");
    }
    
    @Test
    public void testIncrementItemOrderBetween() {
        // Act
        sectionItemRepository.incrementItemOrderBetween(section.getId(), 2, 4);
        entityManager.clear();
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(section.getId());
        items.sort(Comparator.comparing(SectionItem::getItemOrder));
    
        // Assert
        assertEquals(1, items.get(0).getItemOrder(), "First item should have order 1");
        assertEquals(3, items.get(1).getItemOrder(), "Second item should have order 3");
        assertEquals(4, items.get(2).getItemOrder(), "Third item should have order 4");
    }
    
    @Test
    public void testDecrementItemOrderBetween() {
        // Act
        sectionItemRepository.decrementItemOrderBetween(section.getId(), 4, 2);
        entityManager.clear();
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(section.getId());
        items.sort(Comparator.comparing(SectionItem::getItemOrder));
    
        // Assert
        assertEquals(1, items.get(0).getItemOrder(), "First item should have order 1");
        assertEquals(2, items.get(1).getItemOrder(), "Second item should have order 2 (unchanged)");
        assertEquals(2, items.get(2).getItemOrder(), "Third item should have order 2");
    }
    
    @Test
    public void testDeleteAllBySectionId() {
        // Act
        sectionItemRepository.deleteAllBySectionId(section.getId());
        entityManager.clear();
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(section.getId());
    
        // Assert
        assertTrue(items.isEmpty(), "All items should be deleted");
    }
}