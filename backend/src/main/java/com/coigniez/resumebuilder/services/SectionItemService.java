package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRepository;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRepository;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionItemService implements CrudService<SectionItemResponse, SectionItemRequest> {

    private final SectionItemRepository sectionItemRepository;
    private final SectionRepository sectionRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final SectionItemMapper sectionitemMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Long create(SectionItemRequest request) {   
        Section section = sectionRepository.findById(request.getSectionId())
            .orElseThrow(() -> new EntityNotFoundException("Section not found"));

        SectionItem sectionItem = sectionitemMapper.toEntity(request);
        sectionItem.setId(null);
    
        // Find the maximum itemOrder in the section
        Integer maxOrder = sectionItemRepository.findMaxItemOrderBySectionId(section.getId());
        int newOrder = request.getItemOrder() == null ? (maxOrder == null ? 1 : maxOrder + 1) : request.getItemOrder();

        // Shift other items
        incrementItemOrder(section.getId(), newOrder, (maxOrder == null ? 0 : maxOrder) +1);
    
        sectionItem.setItemOrder(newOrder);
        section.addSectionItem(sectionItem);

        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
            .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
        
        sectionItem.setLatexMethod(latexMethod);
    
        return sectionItemRepository.save(sectionItem).getId();
    }

    public Long createPicture(MultipartFile file, SectionItemRequest request) {
        String path = fileStorageService.saveFile(file, securityUtils.getUserName());
        Map<String, Object> data = request.getData();
        data.put("path", path);
        request.setData(data);
        
        return create(request);
    }


    @Override
    public SectionItemResponse get(Long id) {
        return sectionItemRepository.findById(id)
            .map(sectionitemMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
    }

    @Override
    public void update(Long id, SectionItemRequest request) {
        SectionItem sectionItem = sectionItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
        Long sectionId = sectionItem.getSection().getId();
        
        // Shift other items
        if (!sectionItem.getItemOrder().equals(request.getItemOrder())) {
            if (request.getItemOrder() > sectionItem.getItemOrder()) {
                decrementItemOrder(sectionId, request.getItemOrder(), sectionItem.getItemOrder());
            } else {
                incrementItemOrder(sectionId, request.getItemOrder(), sectionItem.getItemOrder());
            }
        }

        // Update the entity
        sectionitemMapper.updateEntity(sectionItem, request);

        //TODO: Update latexMethod
    
        sectionItemRepository.save(sectionItem);
    }

    @Override
    public void delete(Long id) {
        SectionItem sectionItem = sectionItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
        Long sectionId = sectionItem.getSection().getId();
    
        sectionItemRepository.deleteById(id);
        
        // Shift other items
        Integer maxOrder = sectionItemRepository.findMaxItemOrderBySectionId(sectionId);
        decrementItemOrder(sectionId, sectionItem.getItemOrder(), maxOrder+1);
    }

    public List<SectionItem> getAll(Long id) {
        return sectionItemRepository.findAllBySectionId(id);
    }
    
    public void deleteAllBySectionId(Long sectionId) {
        sectionItemRepository.deleteAllBySectionId(sectionId);
    }

    /*
     * Increment the itemOrder for all items in the section starting from startOrder
     */
    @Transactional
    private void incrementItemOrder(Long sectionId, int newOrder, int oldOrder) {
        sectionItemRepository.incrementItemOrderBetween(sectionId, newOrder, oldOrder);
        refreshSectionItems(sectionId);
    }

    @Transactional
    private void decrementItemOrder(Long sectionId, int newOrder, int oldOrder) {
        sectionItemRepository.decrementItemOrderBetween(sectionId, newOrder, oldOrder);
        refreshSectionItems(sectionId);
    }

    private void refreshSectionItems(Long sectionId) {
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        for (SectionItem item : items) {
            entityManager.refresh(item);
        }
    }
}