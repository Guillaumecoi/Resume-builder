package com.coigniez.resumebuilder.model.section;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.resume.Resume;
import com.coigniez.resumebuilder.model.resume.ResumeRepository;
import com.coigniez.resumebuilder.model.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.model.sectionitem.SectionItemService;

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

    public Long create(Long parentId, SectionRequest request, Authentication user) {
        hasAccessResume(parentId, user);

        Section section = sectionMapper.toEntity(request);
        Resume resume = resumeRepository.findById(parentId).orElseThrow();
        section.setResume(resume);
        Long sectionId = sectionRepository.save(section).getId();

        Section createdSection = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException(""));

        createSectionItems(createdSection, request.sectionItems());

        return sectionId;
    }

    public SectionResponse get(Long id, Authentication user) {
        hasAccess(id, user);
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return sectionMapper.toDto(section);
    }

    public void update(Long id, SectionRequest request, Authentication user) {
        hasAccess(id, user);
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        Section UpdatedSection = sectionMapper.toEntity(request);
        BeanUtils.copyProperties(UpdatedSection, existingSection, "id", "resume", "sectionItems");
        sectionRepository.save(existingSection);

        sectionItemService.deleteAllBySectionId(id);
        createSectionItems(existingSection, request.sectionItems());
    }

    public void delete(Long id, Authentication user) {
        hasAccess(id, user);
        sectionRepository.deleteById(id);
    }

    private void createSectionItems(Section section, List<SectionItemRequest> sectionItems) {
        if(sectionItems == null) {
            return;
        }
        sectionItems.stream()
                .map(item -> sectionItemService.create(section, item));
    }

    private void hasAccess(Long id, Authentication user) {
        String owner = sectionRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (!owner.equals(user.getName())) {
            throw new AccessDeniedException("");
        }
    }

    private void hasAccessResume(Long id, Authentication user) {
        String owner = resumeRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (!owner.equals(user.getName())) {
            throw new AccessDeniedException("");
        }
    }
}
