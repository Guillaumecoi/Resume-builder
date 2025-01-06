package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.domain.section.dtos.SectionUpdateReq;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.ResumeRepository;
import com.coigniez.resumebuilder.repository.SectionRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.ParentRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService
        implements ParentEntityService<SectionCreateReq, SectionUpdateReq, SectionResp, Long> {

    private final SectionRepository sectionRepository;
    private final ResumeRepository resumeRepository;
    private final SectionMapper sectionMapper;
    private final SecurityUtils securityUtils;
    private final ParentRepositoryUtil parentRepositoryUtil;

    @Override
    public Long create(SectionCreateReq request) {
        // Check if the user has access to the resume
        securityUtils.hasAccessResume(request.getResumeId());

        // Create the section entity
        Section section = sectionMapper.toEntity(request);

        // Add the section to the resume
        resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", request.getResumeId()))
                .addSection(section);

        // Save the section
        return sectionRepository.save(section).getId();
    }

    @Override
    public SectionResp get(Long id) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(id);

        // Retrieve the section
        return sectionRepository.findById(id)
                .map(sectionMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", id));
    }

    @Override
    public void update(SectionUpdateReq request) {
        // Check if the user has access to the section
        securityUtils.hasAccessSection(request.getId());

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
        resume.removeSection(section);

        // Delete the section
        sectionRepository.deleteById(id);
    }

    @Override
    public List<SectionResp> getAllByParentId(Long resumeId) {
        // Check if the user has access to the resume
        securityUtils.hasAccessResume(resumeId);

        return parentRepositoryUtil.findAllByParentId(Section.class, Resume.class, resumeId, null)
                .stream().map(sectionMapper::toDto).toList();
    }

    @Override
    public void removeAllByParentId(Long resumeId) {
        // Check if the user has access to the resume
        securityUtils.hasAccessResume(resumeId);
        // Clear the sections from the resume
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", resumeId));
        resume.clearSections();

        // Save the resume
        resumeRepository.save(resume);
    }
}
