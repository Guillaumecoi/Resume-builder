package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResponse;
import com.coigniez.resumebuilder.domain.section.dtos.UpdateSectionRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.ResumeRepository;
import com.coigniez.resumebuilder.repository.SectionItemRepository;
import com.coigniez.resumebuilder.repository.SectionRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService
        implements ParentEntityService<CreateSectionRequest, UpdateSectionRequest, SectionResponse, Long> {

    private final SectionRepository sectionRepository;
    private final SectionItemRepository sectionItemRepository;
    private final ResumeRepository resumeRepository;
    private final SectionItemService sectionItemService;
    private final SectionMapper sectionMapper;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(CreateSectionRequest request) {
        // Check if the user has access to the resume
        securityUtils.hasAccessResume(request.getResumeId());

        // Create the section entity
        Section section = sectionMapper.toEntity(request);

        // Add the section to the resume
        resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", request.getResumeId()))
                .addSection(section);

        // Save the section
        Long sectionId = sectionRepository.save(section).getId();

        // Create the section items
        if (request.getSectionItems() != null) {
            for (CreateSectionItemRequest sectionItemRequest : request.getSectionItems()) {
                sectionItemRequest.setSectionId(sectionId);
                sectionItemService.create(sectionItemRequest);
            }
        }

        // Return the section id
        return sectionId;
    }

    @Override
    public SectionResponse get(Long id) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(id);

        // Retrieve the section
        return sectionRepository.findByIdWithOrderedItems(id)
                .map(sectionMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", id));
    }

    @Override
    public void update(UpdateSectionRequest request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(request.getId());

        // Create and update section items
        for (CreateSectionItemRequest sectionItemRequest : request.getCreateSectionItems()) {
            sectionItemRequest.setSectionId(request.getId());
            sectionItemService.create(sectionItemRequest);
        }
        for (UpdateSectionItemRequest sectionItemRequest : request.getUpdateSectionItems()) {
            // Check if the section item belongs to the section
            if (sectionItemRequest.getId() == null) {
                throw new IllegalArgumentException("Section item id is required");
            }
            if (sectionItemRepository.findById(sectionItemRequest.getId())
                    .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", sectionItemRequest.getId()))
                    .getSection().getId() != request.getId()) {
                throw new IllegalArgumentException("Section item does not belong to the section");
            }

            sectionItemService.update(sectionItemRequest);
        }

        // retrieve and update the existing entity
        Section existingSection = sectionRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", request.getId()));
        sectionMapper.updateEntity(existingSection, request);

        // Save the updated entity
        sectionRepository.save(existingSection);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(id);

        // Remove the section from the resume
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", id));
        Resume resume = section.getResume();
        resume.getSections().remove(section);

        // Delete the section
        sectionRepository.deleteById(id);
    }

    @Override
    public List<SectionResponse> getAllByParentId(Long resumeId) {
        return sectionRepository.findAllByResumeId(resumeId).stream()
                .map(sectionMapper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long resumeId) {
        // Clear the sections from the resume
        resumeRepository.findById(resumeId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", resumeId))
                .clearSections();

        // Delete the sections
        sectionRepository.deleteAll(sectionRepository.findAllByResumeId(resumeId));

    }
}
