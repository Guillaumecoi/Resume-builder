package com.coigniez.resumebuilder.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.common.PageResponse;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.resume.ResumeMapper;
import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeDetailResponse;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeResponse;
import com.coigniez.resumebuilder.domain.resume.dtos.UpdateResumeRequest;
import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.section.dtos.UpdateSectionRequest;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.repository.ResumeRepository;
import com.coigniez.resumebuilder.repository.SectionRepository;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService
        implements CrudService<CreateResumeRequest, UpdateResumeRequest, ResumeDetailResponse, Long> {

    private final ResumeRepository resumeRepository;
    private final SectionRepository sectionRepository;
    private final SectionService sectionService;
    private final ResumeMapper resumeMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(CreateResumeRequest request) {
        return resumeRepository.save(resumeMapper.toEntity(request)).getId();
    }

    @Override
    public ResumeDetailResponse get(Long id) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(id);

        // Get the resume entity
        return resumeRepository.findById(id)
                .map(resumeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Resume with id " + id + " not found"));
    }

    @Override
    public void update(UpdateResumeRequest request) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(request.getId());

        // Create and update sections
        for (CreateSectionRequest section : request.getCreateSections()) {
            section.setResumeId(request.getId());
            sectionService.create(section);
        }
        for (UpdateSectionRequest section : request.getUpdateSections()) {
            // Check if the section id is provided and if it belongs to the resume
            if (section.getId() == null) {
                throw new IllegalArgumentException("Section id must be provided");
            }
            if (sectionRepository.findById(section.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Section with id " + section.getId() + " not found"))
                    .getResume().getId() != request.getId()) {
                throw new IllegalArgumentException("Section does not belong to the resume");
            }

            sectionService.update(section);
        }

        // Update the resume entity
        Resume resume = resumeRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Resume with id " + request.getId() + " not found"));
        resumeMapper.updateEntity(resume, request);
        // Save the updated entity
        resumeRepository.save(resume);
    }

    @Override
    public void delete(Long id) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(id);

        // Delete the resume entity and remove the picture
        removePicture(resumeRepository.findById(id).orElseThrow());
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
    public PageResponse<ResumeResponse> getAll(int page, int size, String order) {
        // Get the page of resumes
        // TODO: Make order an enum and implement ascending/descending
        Pageable pageable = PageRequest.of(page, size, Sort.by(order).descending());
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
     * Upload a picture for the resume
     * 
     * @param id   the id of the resume
     * @param file the picture file
     */
    public void uploadPicture(Long id, MultipartFile file) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(id);

        // Get the resume entity
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resume with id " + id + " not found"));

        // Remove the existing picture (if there is one) and save the new one
        removePicture(resume);
        String picture = fileStorageService.saveFile(file, securityUtils.getUserName());
        resume.setPicture(picture);

        // Save the updated entity
        resumeRepository.save(resume);
    }

    /**
     * Delete all the resumes of the connected user
     */
    public void deleteAll() {
        // Remove all the pictures
        fileStorageService.deleteAllUserFiles(securityUtils.getUserName());

        // Remove all the resumes
        resumeRepository.deleteAllByCreatedBy(securityUtils.getUserName());
    }

    /**
     * Remove the picture of the resume
     * 
     * @param resume the resume
     */
    private void removePicture(Resume resume) {
        if (resume.getPicture() != null) {
            fileStorageService.deleteFile(resume.getPicture());
        }
    }
}