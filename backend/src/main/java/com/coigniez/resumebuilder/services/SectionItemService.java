package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
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
        implements ParentEntityService<CreateSectionItemRequest, UpdateSectionItemRequest, SectionItemResponse, Long> {

    private final SectionItemRepository sectionItemRepository;
    private final SubSectionRepository subSectionRepository;
    private final SectionItemMapper sectionitemMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    @Autowired
    private OrderableRepositoryUtil orderableRepositoryUtil;

    @Override
    public Long create(CreateSectionItemRequest request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSubSection(request.getSubSectionId());

        // Get the section and latexMethod
        SubSection section = subSectionRepository.findById(request.getSubSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", request.getSubSectionId()));

        // Find the maximum itemOrder in the section
        int maxOrder = orderableRepositoryUtil
                .findMaxItemOrderByParentId(SectionItem.class, Section.class, section.getId());
        int newOrder = request.getItemOrder() == null ? maxOrder + 1 : request.getItemOrder();

        // Shift the order
        orderableRepositoryUtil.updateItemOrder(SectionItem.class, Section.class, section.getId(),
                newOrder, maxOrder + 1);

        // Create the entity from the request
        request.setItemOrder(newOrder);
        SectionItem sectionItem = sectionitemMapper.toEntity(request);

        // Add the sectionItem to the section and latexMethod
        section.addSectionItem(sectionItem);

        // Save the item
        return sectionItemRepository.save(sectionItem).getId();
    }

    /**
     * Create a picture item
     * 
     * @param file    the file that contains the picture
     * @param request the section item request for the picture
     * @return the id of the created item
     */
    public Long createPicture(MultipartFile file, CreateSectionItemRequest request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(request.getSubSectionId());

        // Save the file to the file storage and add the path to the request
        String path = fileStorageService.saveFile(file, securityUtils.getUserName());
        ((Picture) request.getItem()).setPath(path);

        // Create the item
        return create(request);
    }

    @Override
    public SectionItemResponse get(Long id) {
        // Check if the user has access to the sectionItem
        securityUtils.hasAccessSectionItem(id);
        // Get the item
        return sectionItemRepository.findById(id)
                .map(sectionitemMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", id));
    }

    @Override
    public void update(UpdateSectionItemRequest request) {
        // Check if the user has access to the sectionItem
        securityUtils.hasAccessSectionItem(request.getId());

        // Get the entity
        SectionItem sectionItem = sectionItemRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", request.getId()));
        Long sectionId = sectionItem.getSection().getId();

        // Shift other items
        orderableRepositoryUtil.updateItemOrder(SectionItem.class, Section.class, sectionId, request.getItemOrder(),
                sectionItem.getItemOrder());

        // Update the entity
        sectionitemMapper.updateEntity(sectionItem, request);

        // TODO: Update the section

        // save the updated item
        sectionItemRepository.save(sectionItem);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the sectionItem
        securityUtils.hasAccessSectionItem(id);

        // Get the item
        SectionItem sectionItem = sectionItemRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", id));
        long sectionId = sectionItem.getSection().getId();

        // Remove the item from the section
        sectionItem.getSection().removeSectionItem(sectionItem);

        // Delete the item
        sectionItemRepository.deleteById(id);

        // Shift other items
        int maxOrder = sectionItemRepository.findMaxItemOrderBySectionId(sectionId).orElse(0);
        orderableRepositoryUtil.updateItemOrder(SectionItem.class, Section.class, sectionId,
                maxOrder + 1, sectionItem.getItemOrder());
    }

    @Override
    public List<SectionItemResponse> getAllByParentId(Long id) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(id);

        List<SectionItem> items = sectionItemRepository.findAllBySectionId(id);
        return items.stream()
                .map(sectionitemMapper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long id) {
        // Check if the user has access to the section
        securityUtils.hasAccessSubSection(id);

        SubSection section = subSectionRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", id));

        section.clearSectionItems();
        sectionItemRepository.deleteAllBySectionId(id);
    }
}