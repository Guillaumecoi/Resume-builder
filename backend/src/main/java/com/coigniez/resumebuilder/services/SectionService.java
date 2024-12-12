package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
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
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionService implements CrudService<SectionResponse, SectionRequest> {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final ResumeRepository resumeRepository;
    private final SectionItemService sectionItemService;
    private final SecurityUtils securityUtils;

    public Long create(SectionRequest request) {
        hasAccessResume(request.getResumeId());

        Section section = sectionMapper.toEntity(request);
        Resume resume = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"));
        resume.addSection(section);
        Long sectionId = sectionRepository.save(section).getId();

        createSectionItems(sectionId, request.getSectionItems());

        return sectionId;
    }

    public SectionResponse get(Long id) {
        hasAccess(id);
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return sectionMapper.toDto(section);
    }

    public void update(Long id, SectionRequest request) {
        hasAccess(id);
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
                
        // Update basic properties
        Section updatedSection = sectionMapper.toEntity(request);
        BeanUtils.copyProperties(updatedSection, existingSection, "id", "resume", "items", "columnSections");
        
        // Clear and update items in one transaction
        existingSection.getItems().clear();
        if (request.getSectionItems() != null) {
            createSectionItems(existingSection.getId(), request.getSectionItems());
        }
        
        sectionRepository.save(existingSection);
    }

    public void delete(Long id) {
        hasAccess(id);
        Section section = sectionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(""));

        Resume resume = section.getResume();
        resume.getSections().remove(section);

        sectionRepository.deleteById(id);
    }

    public List<SectionResponse> getAll(Long resumeId) {
        List<Section> sections = sectionRepository.findAllByResumeId(resumeId);
        return sections.stream()
                .map(sectionMapper::toDto)
                .toList();
    }

    private void createSectionItems(long sectionId, List<SectionItemRequest> sectionItems) {
        if(sectionItems == null) {
            return;
        }
        for (SectionItemRequest sectionItemRequest : sectionItems) {
            sectionItemRequest.setSectionId(sectionId);
            sectionItemService.create(sectionItemRequest);
        }
    }

    private void hasAccess(Long id) {
        String owner = sectionRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (!owner.equals(securityUtils.getUserName())) {
            throw new AccessDeniedException("");
        }
    }

    private void hasAccessResume(Long id) {
        String owner = resumeRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (!owner.equals(securityUtils.getUserName())) {
            throw new AccessDeniedException("");
        }
    }
}
