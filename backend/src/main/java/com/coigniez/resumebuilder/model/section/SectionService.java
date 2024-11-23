package com.coigniez.resumebuilder.model.section;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.resume.Resume;
import com.coigniez.resumebuilder.model.resume.ResumeRepository;
import com.coigniez.resumebuilder.model.resume.ResumeService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionService implements CrudService<SectionResponse, SectionRequest> {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;

        public Long create(Long parentId, SectionRequest request, Authentication user) {
        resumeService.hasAccess(parentId, user);
        Section section = sectionMapper.toEntity(request);
        Resume resume = resumeRepository.findById(parentId).orElseThrow();
        section.setResume(resume);
        return sectionRepository.save(section).getId();
    }

    public SectionResponse get(Long id, Authentication user) {
        hasAccess(id, user);
        Section section = sectionRepository.findById(id).orElseThrow();
        return sectionMapper.toDto(section);
    }

    public void update(Long id, SectionRequest request, Authentication user) {
        hasAccess(id, user);
        Section section = sectionMapper.toEntity(request);
        section.setId(id);
        sectionRepository.save(section);
    }

    public void delete(Long id, Authentication user) {
        hasAccess(id, user);
        sectionRepository.deleteById(id);
    }

    public void hasAccess(Long id, Authentication user) {
        String owner = sectionRepository.findCreatedBy(id);
        if (!owner.equals(user.getName())) {
            throw new AccessDeniedException("");
        }

    }
}
