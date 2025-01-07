package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResp;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemUpdateReq;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.subsection.SubSection;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.SectionItemRepository;
import com.coigniez.resumebuilder.repository.SubSectionRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.OrderableRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionItemService
        implements
        ParentEntityService<SectionItemCreateReq, SectionItemUpdateReq, SectionItemResp, Long> {

    private final SectionItemRepository sectionItemRepository;
    private final SubSectionRepository subSectionRepository;
    private final SectionItemMapper sectionitemMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    private final OrderableRepositoryUtil orderableRepositoryUtil;

    @Override
    public Long create(SectionItemCreateReq request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSubSection(request.getSubSectionId());

        // Get the section and latexMethod
        SubSection section = subSectionRepository.findById(request.getSubSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", request.getSubSectionId()));

        // Save the item
        return addItemToSection(sectionitemMapper.toEntity(request), section);
    }

    /**
     * Create a picture item
     * 
     * @param file    the file that contains the picture
     * @param request the section item request for the picture
     * @return the id of the created item
     */
    public Long createPicture(MultipartFile file, SectionItemCreateReq request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSubSection(request.getSubSectionId());

        // Save the file to the file storage and add the path to the request
        String path = fileStorageService.saveFile(file, securityUtils.getUserName());
        ((Picture) request.getItem()).setPath(path);

        // Create the item
        return create(request);
    }

    @Override
    public SectionItemResp get(Long id) {
        // Check if the user has access to the sectionItem
        securityUtils.hasAccessSectionItem(id);

        return sectionItemRepository.findById(id)
                .map(sectionitemMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", id));
    }

    @Override
    public void update(SectionItemUpdateReq request) {
        // Check if the user has access to the sectionItem
        securityUtils.hasAccessSectionItem(request.getId());

        // Get the entity
        SectionItem sectionItem = sectionItemRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", request.getId()));
        SubSection oldSection = sectionItem.getSubSection();

        // Change subSection if needed
        if (request.getSubSectionId() != oldSection.getId()) {
            // Check if the user has access to the section
            securityUtils.hasAccessSubSection(request.getSubSectionId());

            // Get the section
            SubSection newSection = subSectionRepository.findById(request.getSubSectionId())
                    .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", request.getSubSectionId()));

            // Remove the item from the old section
            deleteItemFromSection(sectionItem, oldSection);
            // Update the entity
            sectionitemMapper.updateEntity(sectionItem, request);
            sectionItem.setId(null);
            // Add the item to the new section
            addItemToSection(sectionItem, newSection);
        } else {
            // Shift other items
            updateItemOrder(request.getItemOrder(), sectionItem.getItemOrder(), oldSection.getId());
            // Update the entity
            sectionitemMapper.updateEntity(sectionItem, request);
            // save the updated item
            sectionItemRepository.save(sectionItem);
        }
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the sectionItem
        securityUtils.hasAccessSectionItem(id);

        // Get the item
        SectionItem sectionItem = sectionItemRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", id));

        // Remove the item from the section and save the section
        deleteItemFromSection(sectionItem, sectionItem.getSubSection());
    }

    @Override
    public List<SectionItemResp> getAllByParentId(Long id) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(id);

        return null;
    }

    @Override
    public void removeAllByParentId(Long id) {
        // Check if the user has access to the section
        securityUtils.hasAccessSubSection(id);

        SubSection section = subSectionRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", id));

        section.clearSectionItems();
    }

    /*
     * Remove the item from the section and update the order of the other items
     */
    private void deleteItemFromSection(SectionItem sectionItem, SubSection section) {
        section.removeSectionItem(sectionItem);
        updateItemOrder(null, sectionItem.getItemOrder(), section.getId());
        subSectionRepository.save(section);
    }

    /*
     * Add the item to the section
     */
    private Long addItemToSection(SectionItem sectionItem, SubSection section) {
        int newOrder = updateItemOrder(sectionItem.getItemOrder(), null, section.getId());
        if (sectionItem.getItemOrder() == null) {
            sectionItem.setItemOrder(newOrder);
        }
        section.addSectionItem(sectionItem);
        long sectionItemId = sectionItemRepository.save(sectionItem).getId();
        return sectionItemId;
    }

    /*
     * Update the order of the items in the section
     */
    private int updateItemOrder(Integer newOrder, Integer oldOrder, Long sectionId) {
        return orderableRepositoryUtil.updateItemOrder(SectionItem.class, SubSection.class, sectionId,
                "itemOrder", newOrder, oldOrder);
    }
}