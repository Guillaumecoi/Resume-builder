package com.coigniez.resumebuilder.model.section;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.resume.Resume;
import com.coigniez.resumebuilder.model.resume.ResumeRepository;
import com.coigniez.resumebuilder.model.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.model.sectionitem.SectionItemService;
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

    public Long create(Long parentId, SectionRequest request) {
        hasAccessResume(parentId);

        Section section = sectionMapper.toEntity(request);
        Resume resume = resumeRepository.findById(parentId).orElseThrow();
        section.setResume(resume);
        Long sectionId = sectionRepository.save(section).getId();

        Section createdSection = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException(""));

        createSectionItems(createdSection, request.sectionItems());

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
                .orElseThrow(() -> new EntityNotFoundException(""));
        Section UpdatedSection = sectionMapper.toEntity(request);
        BeanUtils.copyProperties(UpdatedSection, existingSection, "id", "resume", "sectionItems");
        sectionRepository.save(existingSection);

        sectionItemService.deleteAllBySectionId(id);
        createSectionItems(existingSection, request.sectionItems());
    }

    public void delete(Long id) {
        hasAccess(id);
        sectionRepository.deleteById(id);
    }

    private void createSectionItems(Section section, List<SectionItemRequest> sectionItems) {
        if(sectionItems == null) {
            return;
        }
        sectionItems.stream()
                .map(item -> sectionItemService.create(section, item));
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
