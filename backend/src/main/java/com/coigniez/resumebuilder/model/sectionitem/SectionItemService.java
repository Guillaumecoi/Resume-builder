package com.coigniez.resumebuilder.model.sectionitem;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.section.Section;
import com.coigniez.resumebuilder.model.section.SectionRepository;
import com.coigniez.resumebuilder.model.section.SectionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionItemService implements CrudService<SectionItemResponse, SectionItemRequest> {

    private final SectionItemRepository sectionitemRepository;
    private final SectionItemMapper sectionitemMapper;
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;

    public Long create(Long sectionId, SectionItemRequest request, Authentication user) {
        sectionService.hasAccess(sectionId, user);
        Section section = sectionRepository.findById(sectionId)
                                           .orElseThrow(() -> new EntityNotFoundException("Section not found"));
    
        SectionItem sectionItem = sectionitemMapper.toEntity(request);
    
        // Find the maximum itemOrder in the section
        Integer maxOrder = sectionitemRepository.findMaxItemOrderBySectionId(sectionId);
        int newOrder = request.itemOrder() == null ? (maxOrder == null ? 1 : maxOrder + 1) : request.itemOrder();
    
        if (request.itemOrder() != null) {
            // Shift other items
            sectionitemRepository.incrementItemOrderForSection(sectionId, newOrder);
        }
    
        sectionItem.setItemOrder(newOrder);
        sectionItem.setSection(section);
    
        return sectionitemRepository.save(sectionItem).getId();
    }
    

    public SectionItemResponse get(Long id, Authentication user) {
        hasAccess(id, user);
        SectionItem sectionitem = sectionitemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return sectionitemMapper.toDto(sectionitem);
    }

    public void update(Long id, SectionItemRequest request, Authentication user) {
        hasAccess(id, user);
        SectionItem sectionItem = sectionitemRepository.findById(id)
                                  .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
    
        int oldOrder = sectionItem.getItemOrder();
        int newOrder = request.itemOrder();
    
        // Shift orders if necessary
        if (newOrder < oldOrder) {
            sectionitemRepository.incrementItemOrderForRange(sectionItem.getSection().getId(), newOrder, oldOrder - 1);
        } else if (newOrder > oldOrder) {
            sectionitemRepository.decrementItemOrderForRange(sectionItem.getSection().getId(), oldOrder + 1, newOrder);
        }
    
        sectionItem.setItemOrder(newOrder);
        sectionitemRepository.save(sectionItem);
    }
    

    public void delete(Long id, Authentication user) {
        hasAccess(id, user);
        SectionItem sectionItem = sectionitemRepository.findById(id)
                                  .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
    
        sectionitemRepository.decrementItemOrderForSection(sectionItem.getSection().getId(), sectionItem.getItemOrder());
        sectionitemRepository.deleteById(id);
    }
    

    public void hasAccess(Long id, Authentication user) {
        String owner = sectionitemRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (!owner.equals(user.getName())) {
            throw new AccessDeniedException("");
        }
    }
}