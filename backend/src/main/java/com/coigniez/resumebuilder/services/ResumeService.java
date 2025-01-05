package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.common.PageResponse;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.resume.ResumeMapper;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeSimpleResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeUpdateReq;
import com.coigniez.resumebuilder.domain.resume.enums.ResumeOrderBy;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.repository.ResumeRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService
        implements CrudService<ResumeCreateReq, ResumeUpdateReq, ResumeResp, Long> {

    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(ResumeCreateReq request) {
        return resumeRepository.save(resumeMapper.toEntity(request)).getId();
    }

    @Override
    public ResumeResp get(Long id) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(id);

        // Get the resume entity
        return resumeRepository.findById(id)
                .map(resumeMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", id));
    }

    @Override
    public void update(ResumeUpdateReq request) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(request.getId());

        // Update the resume entity
        Resume resume = resumeRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", request.getId()));
        resumeMapper.updateEntity(resume, request);
        // Save the updated entity
        resumeRepository.save(resume);
    }

    @Override
    public void delete(Long id) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(id);
        resumeRepository.deleteById(id);
    }

    /**
     * Get all the resumes of the connected user
     * 
     * @param page  the page number
     * @param size  the page size
     * @param order on what field to order the resumes
     * @return the page of resumes
     */
    public PageResponse<ResumeSimpleResp> getAll(int page, int size, ResumeOrderBy order, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, order.getFieldName()));
        Page<Resume> resumes = resumeRepository.findAllByCreatedBy(pageable, securityUtils.getUserName());

        // Return the page of resumes
        return new PageResponse<>(
                resumes.stream().map(resumeMapper::toSimpleDto).toList(),
                resumes.getNumber(),
                resumes.getSize(),
                resumes.getTotalElements(),
                resumes.getTotalPages(),
                resumes.isFirst(),
                resumes.isLast());
    }

    /**
     * Delete all the resumes of the connected user
     */
    public void deleteAll() {
        // Remove all the pictures
        fileStorageService.deleteAllUserFiles(securityUtils.getUserName());

        // Retrieve and delete all the resumes of the connected user
        List<Resume> userResumes = resumeRepository.findAllByCreatedBy(securityUtils.getUserName());
        resumeRepository.deleteAll(userResumes);
    }

}