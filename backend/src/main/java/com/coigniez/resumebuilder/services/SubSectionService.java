package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemCreateReq;
import com.coigniez.resumebuilder.domain.subsection.SubSection;
import com.coigniez.resumebuilder.domain.subsection.SubSectionMapper;
import com.coigniez.resumebuilder.domain.subsection.dtos.CreateSubSectionRequest;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResponse;
import com.coigniez.resumebuilder.domain.subsection.dtos.UpdateSubSectionRequest;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.SectionRepository;
import com.coigniez.resumebuilder.repository.SubSectionRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.ParentRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubSectionService
        implements ParentEntityService<CreateSubSectionRequest, UpdateSubSectionRequest, SubSectionResponse, Long> {

    private final SubSectionRepository subSectionRepository;
    private final SectionRepository sectionRepository;
    private final SubSectionMapper subSectionMapper;
    private final SectionItemService sectionItemService;
    private final SecurityUtils securityUtils;
    private final ParentRepositoryUtil parentRepositoryUtil;

    @Override
    public Long create(@NotNull CreateSubSectionRequest request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(request.getSectionId());

        // Create the sub-section entity
        SubSection subSection = subSectionMapper.toEntity(request);

        // Add the sub-section to the section
        sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", request.getSectionId()))
                .addSubSection(subSection);
        
        // Save the sub-section
        Long subSectionId = subSectionRepository.save(subSection).getId();

        // Create the section items
        if (request.getSectionItems() != null) {
            for (SectionItemCreateReq sectionItemRequest : request.getSectionItems()) {
                sectionItemRequest.setSubSectionId(subSectionId);
                sectionItemService.create(sectionItemRequest);
            }
        }

        return subSectionId;
    }

    @Override
    public SubSectionResponse get(@NotNull Long id) {
        // Check if the user has access to the sub-section
        securityUtils.hasAccessSubSection(id);

        // Map the sub-section to a response
        return subSectionRepository.findById(id)
                .map(subSectionMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", id));
    }

    @Override
    public void update(@NotNull UpdateSubSectionRequest request) {
        // Check if the user has access to the sub-section
        securityUtils.hasAccessSubSection(request.getId());

        // Update the sub-section entity
        SubSection subSection = subSectionRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", request.getId()));
        subSectionMapper.updateEntity(subSection, request);

        // Save the sub-section
        subSectionRepository.save(subSection);
    }

    @Override
    public void delete(@NotNull Long id) {
        // Check if the user has access to the sub-section
        securityUtils.hasAccessSubSection(id);

        // Remove the sub-section from the section
        SubSection subSection = subSectionRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", id));
        subSection.getSection().removeSubSection(subSection);

        // Delete the sub-section
        subSectionRepository.delete(subSection);
    }

    @Override
    public List<SubSectionResponse> getAllByParentId(Long parentId) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(parentId);
        // Get all the sub-sections
        return parentRepositoryUtil.findAllByParentId(SubSection.class, Section.class, parentId)
                .stream()
                .map(subSectionMapper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long parentId) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(parentId);
        // Remove all the sub-sections
        parentRepositoryUtil.removeAllByParentId(SubSection.class, Section.class, parentId);
    }

}
