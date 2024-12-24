package com.coigniez.resumebuilder.services;

import java.util.List;

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
import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionItemService implements ParentEntityService<CreateSectionItemRequest, UpdateSectionItemRequest, SectionItemResponse, Long> {

    private final SectionItemRepository sectionItemRepository;
    private final SectionRepository sectionRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final SectionItemMapper sectionitemMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    @Autowired
    private EntityManager entityManager;

    @Override
    public Long create(CreateSectionItemRequest request) {
        // Check if the user has access to the section
        hasAccessSection(request.getSectionId());  

        // Get the section and latexMethod
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
            .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));

        // Find the maximum itemOrder in the section
        int maxOrder = sectionItemRepository.findMaxItemOrderBySectionId(section.getId()).orElse(0);
        int newOrder = request.getItemOrder() == null ? maxOrder + 1 : request.getItemOrder();

        // Shift the order
        incrementItemOrder(section.getId(), newOrder, maxOrder +1);

        // Create the entity from the request
        request.setItemOrder(newOrder);
        SectionItem sectionItem = sectionitemMapper.toEntity(request);

        // Add the sectionItem to the section and latexMethod
        section.addSectionItem(sectionItem);
        sectionItem.setLatexMethod(latexMethod);
            
    
        // Save the item
        return sectionItemRepository.save(sectionItem).getId();
    }

    /**
     * Create a picture item
     * 
     * @param file the file that contains the picture
     * @param request the section item request for the picture
     * @return the id of the created item
     */
    public Long createPicture(MultipartFile file, CreateSectionItemRequest request) {
        // Check if the user has access to the section
        hasAccessSection(request.getSectionId());
        
        // Save the file to the file storage and add the path to the request
        String path = fileStorageService.saveFile(file, securityUtils.getUserName());
        ((Picture) request.getItem()).setPath(path);

        // Create the item
        return create(request);
    }

    @Override
    public SectionItemResponse get(Long id) {
        // Check if the user has access to the sectionItem
        hasAccess(id);
        // Get the item
        return sectionItemRepository.findById(id)
            .map(sectionitemMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
    }

    @Override
    public void update(UpdateSectionItemRequest request) {
        // Check if the user has access to the sectionItem
        hasAccess(request.getId());

        // Get the entity
        SectionItem sectionItem = sectionItemRepository.findById(request.getId())
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

        // Update the latexMethod
        if (request.getLatexMethodId() != sectionItem.getLatexMethod().getId()) {
            LatexMethod newLatexMethod = latexMethodRepository.findById(request.getLatexMethodId())
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
            sectionItem.setLatexMethod(newLatexMethod);
        }

        //TODO: Update the section
        
        // save the updated item
        sectionItemRepository.save(sectionItem);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the sectionItem
        hasAccess(id);

        // Get the item
        SectionItem sectionItem = sectionItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
        long sectionId = sectionItem.getSection().getId();

        // Remove the item from the section
        sectionItem.getSection().removeSectionItem(sectionItem);
    
        // Delete the item
        sectionItemRepository.deleteById(id);
        
        // Shift other items
        int maxOrder = sectionItemRepository.findMaxItemOrderBySectionId(sectionId).orElse(0);
        decrementItemOrder(sectionId, maxOrder + 1, sectionItem.getItemOrder());
    }

    @Override
    public List<SectionItemResponse> getAllByParentId(Long id) {
        // Check if the user has access to the section
        hasAccessSection(id);

        List<SectionItem> items = sectionItemRepository.findAllBySectionId(id);
        return items.stream()
            .map(sectionitemMapper::toDto)
            .toList();
    }

    @Override
    public void removeAllByParentId(Long id) {
        // Check if the user has access to the section
        hasAccessSection(id);

        Section section = sectionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Section not found"));

        // Remove all items from the section
        section.clearSectionItems();

        sectionItemRepository.deleteAllBySectionId(id);
    }
    
    /**
     * Increment the itemOrder for all items in the section starting from startOrder
     * All other items with order >= newOrder and < oldOrder will be incremented
     * 
     * @param sectionId the section id
     * @param newOrder the new order
     * @param oldOrder the old order
     */
    private void incrementItemOrder(Long sectionId, int newOrder, int oldOrder) {
        sectionItemRepository.incrementItemOrderBetween(sectionId, newOrder, oldOrder);
        refreshSectionItems(sectionId);
    }

    /**
     * Decrement the itemOrder for all items in the section starting from startOrder
     * All other items with order > oldOrder and <= newOrder will be decremented
     * 
     * @param sectionId the section id
     * @param newOrder the new order
     * @param oldOrder the old order
     */
    private void decrementItemOrder(Long sectionId, int newOrder, int oldOrder) {
        sectionItemRepository.decrementItemOrderBetween(sectionId, newOrder, oldOrder);
        refreshSectionItems(sectionId);
    }

    /**
     * Refresh the section items
     * 
     * @param sectionId the section id
     */
    private void refreshSectionItems(Long sectionId) {
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        for (SectionItem item : items) {
            entityManager.refresh(item);
        }
    }

    /**
     * Check if the user has access to the sectionItem
     * 
     * @param id the id of the sectionItem
     */
    private void hasAccess(Long id) {
        String owner = sectionItemRepository.findCreatedBy(id)
            .orElseThrow(() -> new EntityNotFoundException("Accompanying resume is not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    /**
     * Check if the user has access to the section
     * 
     * @param id the id of the section
     */
    private void hasAccessSection(Long id) {
        String owner = sectionRepository.findCreatedBy(id)
            .orElseThrow(() -> new EntityNotFoundException("Accompanying resume is not found"));
        securityUtils.hasAccess(List.of(owner));
    }
}