package com.coigniez.resumebuilder.domain.sectionitem;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.repository.SectionItemRepository;
import com.coigniez.resumebuilder.repository.SectionRepository;

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
                .item(Skill.builder().name("Java").build())
                .build();

        SectionItem item2 = SectionItem.builder()
                .section(section)
                .itemOrder(2)
                .item(Skill.builder().name("Python").build())
                .build();

        SectionItem item3 = SectionItem.builder()
                .section(section)
                .itemOrder(3)
                .item(Skill.builder().name("HTML").build())
                .build();

        sectionItemRepository.saveAll(List.of(item1, item2, item3));
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