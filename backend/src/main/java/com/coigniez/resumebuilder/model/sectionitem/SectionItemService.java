package com.coigniez.resumebuilder.model.sectionitem;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.model.section.Section;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionItemService {

    private final SectionItemRepository sectionitemRepository;
    private final SectionItemMapper sectionitemMapper;

    public Long create(Section section, SectionItemRequest request) {   
        SectionItem sectionItem = sectionitemMapper.toEntity(request);
    
        // Find the maximum itemOrder in the section
        Integer maxOrder = sectionitemRepository.findMaxItemOrderBySectionId(section.getId());
        int newOrder = request.itemOrder() == null ? (maxOrder == null ? 1 : maxOrder + 1) : request.itemOrder();
    
        if (request.itemOrder() != null) {
            // Shift other items
            sectionitemRepository.incrementItemOrderForSection(section.getId(), newOrder);
        }
    
        sectionItem.setItemOrder(newOrder);
        sectionItem.setSection(section);
    
        return sectionitemRepository.save(sectionItem).getId();
    }

    public List<SectionItem> getAll(Long id) {
        return sectionitemRepository.findAllBySectionId(id);
    }
    

    public void deleteAllBySectionId(Long sectionId) {
        sectionitemRepository.deleteAllBySectionId(sectionId);
    }
}