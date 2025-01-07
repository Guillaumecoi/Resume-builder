package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.subsection.SubSection;
import com.coigniez.resumebuilder.domain.subsection.SubSectionMapper;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResp;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionUpdateReq;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.SectionRepository;
import com.coigniez.resumebuilder.repository.SubSectionRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.ParentRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubSectionService
        implements ParentEntityService<SubSectionCreateReq, SubSectionUpdateReq, SubSectionResp, Long> {

    private final SubSectionRepository subSectionRepository;
    private final SectionRepository sectionRepository;
    private final SubSectionMapper subSectionMapper;
    private final SecurityUtils securityUtils;
    private final ParentRepositoryUtil parentRepositoryUtil;

    @Override
    public Long create(SubSectionCreateReq request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(request.getSectionId());

        // Create the sub-section entity
        SubSection subSection = subSectionMapper.toEntity(request);

        // Add the sub-section to the section
        sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", request.getSectionId()))
                .addSubSection(subSection);

        // Save the sub-section
        return subSectionRepository.save(subSection).getId();
    }

    @Override
    public SubSectionResp get(Long id) {
        // Check if the user has access to the sub-section
        securityUtils.hasAccessSubSection(id);

        // Map the sub-section to a response
        return subSectionRepository.findById(id)
                .map(subSectionMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", id));
    }

    @Override
    public void update(SubSectionUpdateReq request) {
        // Check if the user has access to the sub-section
        securityUtils.hasAccessSubSection(request.getId());

        // Update the sub-section entity
        SubSection subSection = subSectionRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SubSection", request.getId()));
        subSectionMapper.updateEntity(subSection, request);

        // Change the section if needed
        if (!subSection.getSection().getId().equals(request.getSectionId())) {
            // Check Access to the new section
            securityUtils.hasAccessSection(request.getSectionId());
            // Remove the sub-section from the old section
            subSection.getSection().removeSubSection(subSection);
            // Add the sub-section to the new section
            sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", request.getSectionId()))
                    .addSubSection(subSection);
        }

        // Save the sub-section
        subSectionRepository.save(subSection);
    }

    @Override
    public void delete(Long id) {
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
    public List<SubSectionResp> getAllByParentId(Long parentId) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(parentId);
        // Get all the sub-sections
        return parentRepositoryUtil.findAllByParentId(SubSection.class, Section.class, parentId, null)
                .stream().map(subSectionMapper::toDto).toList();
    }

    @Override
    public void removeAllByParentId(Long parentId) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(parentId);
        // Clear the sub-sections from the section
        Section section = sectionRepository.findById(parentId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", parentId));
        section.clearSubSections();

        // Save the section
        sectionRepository.save(section);
    }

}
