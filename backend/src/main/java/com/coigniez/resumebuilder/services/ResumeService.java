package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.common.PageResponse;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.resume.ResumeDetailResponse;
import com.coigniez.resumebuilder.domain.resume.ResumeMapper;
import com.coigniez.resumebuilder.domain.resume.ResumeRepository;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeResponse;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService implements CrudService<ResumeRequest, ResumeDetailResponse, Long> {

    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final SectionService sectionService;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(ResumeRequest request) {
        return resumeRepository.save(resumeMapper.toEntity(request)).getId();
    }

    @Override
    public ResumeDetailResponse get(Long id) {
        // Check if the connected user has access to the resume
        hasAccess(id);

        // Get the resume entity
        return resumeRepository.findById(id)
                .map(resumeMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Resume with id " + id + " not found"));
    }

    @Override
    public void update(ResumeRequest request) {
        // Check if the connected user has access to the resume
        hasAccess(request.getId());

        for(SectionRequest section : request.getSections()) {
            if (section.getId() == null) {
                section.setResumeId(request.getId());
                sectionService.create(section);
            } else {
                sectionService.update(section);
            }
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
        hasAccess(id);

        // Delete the resume entity and remove the picture
        removePicture(resumeRepository.findById(id).orElseThrow());
        resumeRepository.deleteById(id);
    }
    
    /**
     * Get all the resumes of the connected user
     * 
     * @param page the page number
     * @param size the page size
     * @param order on what field to order the resumes
     * @return the page of resumes
     */
    public PageResponse<ResumeResponse> getAll(int page, int size, String order) {
        // Get the page of resumes
        //TODO: Make order an enum and implement ascending/descending
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
            resumes.isLast()
        );
    }
 
    /**
     * Upload a picture for the resume
     * 
     * @param id the id of the resume
     * @param file the picture file
     */
    public void uploadPicture(Long id, MultipartFile file) {
        // Check if the connected user has access to the resume
        hasAccess(id);

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
     * Check if the connected user has access to the resume
     * 
     * @param id the id of the resume
     * @param connectedUser the connected user
     * @throws AccessDeniedException if the connected user does not have access to the resume
     */
    private void hasAccess(Long id) throws AccessDeniedException {
        String owner = resumeRepository.findCreatedBy(id).orElseThrow(() -> new EntityNotFoundException("Resume with id " + id + " not found"));
        securityUtils.hasAccess(List.of(owner));
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