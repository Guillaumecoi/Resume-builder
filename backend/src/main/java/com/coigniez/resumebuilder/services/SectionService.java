package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.resume.ResumeRepository;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService implements ParentEntityService<SectionRequest, SectionResponse, Long> {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final ResumeRepository resumeRepository;
    private final SectionItemService sectionItemService;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(SectionRequest request) {
        // Check if the user has access to the resume
        hasAccessResume(request.getResumeId());

        // Create the section entity
        Section section = sectionMapper.toEntity(request);

        // Add the section to the resume
        resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"))
                .addSection(section);

        // Save the section
        Long sectionId = sectionRepository.save(section).getId();

        // Create the section items
        if (request.getSectionItems() != null) {
            for (SectionItemRequest sectionItemRequest : request.getSectionItems()) {
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
        hasAccess(id);

        // Retrieve the section
        return sectionRepository.findByIdWithOrderedItems(id)
                .map(sectionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(""));
    }

    @Override
    public void update(SectionRequest request) {
        // Check if the user has access to the section
        hasAccess(request.getId());

        // Update section items
        for (SectionItemRequest sectionItemRequest : request.getSectionItems()) {
            if (sectionItemRequest.getId() == null) {
                sectionItemRequest.setSectionId(request.getId());
                sectionItemService.create(sectionItemRequest);
            } else {
                sectionItemService.update(sectionItemRequest);
            }
        }

        // retrieve and update the existing entity
        Section existingSection = sectionRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
        sectionMapper.updateEntity(existingSection, request);

        // Save the updated entity
        sectionRepository.save(existingSection);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the section
        hasAccess(id);

        // Remove the section from the resume
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
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
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"))
                .clearSections();

        // Delete the sections
        sectionRepository.deleteAll(sectionRepository.findAllByResumeId(resumeId));
        
    }

    /**
     * Check if the user has access to the section
     * 
     * @param id the section id
     * @throws AccessDeniedException if the user does not have access
     */
    private void hasAccess(Long id) {
        String owner = sectionRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        securityUtils.hasAccess(List.of(owner));
    }

    /**
     * Check if the user has access to the resume
     * 
     * @param id the resume id
     * @throws AccessDeniedException if the user does not have access
     */
    private void hasAccessResume(Long id) {
        String owner = resumeRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        securityUtils.hasAccess(List.of(owner));
    }
}
