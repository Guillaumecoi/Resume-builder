package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
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
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ResumeService implements CrudService<ResumeDetailResponse, ResumeRequest> {

    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    public Long create(ResumeRequest request) {
        Resume resume = resumeMapper.toEntity(request);
        return resumeRepository.save(resume).getId();
    }

    public ResumeDetailResponse get(Long id) {
        hasAccess(id);
        Resume resume = resumeRepository.findById(id).orElseThrow();
        log.debug("retrieved from repository: " + resume.toString());
        return resumeMapper.toDto(resume);
    }

    public void update(Long id, ResumeRequest request) {
        hasAccess(id);
        Resume existingResume = resumeRepository.findById(id).orElseThrow();
        Resume updatedResume = resumeMapper.toEntity(request);
        
        // Copy the properties from the updated resume to the existing resume
        BeanUtils.copyProperties(updatedResume, existingResume, "id", "createdBy", "createdDate", "lastModifiedDate", "sections", "layouts", "picture");
    
        resumeRepository.save(existingResume);
    }

    public void delete(Long id) {
        hasAccess(id);
        try {
            removePicture(resumeRepository.findById(id).orElseThrow());
            resumeRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        }
    }
        

    public PageResponse<ResumeResponse> getAll(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(order).descending());
        Page<Resume> resumes = resumeRepository.findAllByCreatedBy(pageable, securityUtils.getUserName());
        // Convert the page of resumes to a list of resume responses
        List<ResumeResponse> resumeResponses = resumes.stream()
            .map(resumeMapper::toSimpleDto)
            .toList();
        // Return a page response with the list of resume responses
        return new PageResponse<>(
            resumeResponses, 
            resumes.getNumber(),
            resumes.getSize(),
            resumes.getTotalElements(),
            resumes.getTotalPages(),
            resumes.isFirst(),
            resumes.isLast()
        );
    }
 
    public void uploadPicture(Long id, MultipartFile file) {
        hasAccess(id);
        Resume resume = resumeRepository.findById(id).orElseThrow();
        removePicture(resume);
        log.debug(null == file ? "null file" : "file name: " + file.getOriginalFilename());
        String picture = fileStorageService.saveFile(file, securityUtils.getUserName());
        resume.setPicture(picture);
        resumeRepository.save(resume);
        log.debug(picture + " saved to resume with id " + id);
    }

    public void deleteAll() {
        try {
            List<Resume> resumes = resumeRepository.findAllByCreatedBy(securityUtils.getUserName());
            for (Resume resume : resumes) {
                removePicture(resume);
            }
            resumeRepository.deleteAllByCreatedBy(securityUtils.getUserName());
        } catch (Exception e) {
            throw e;
        }
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
        String connectedUser = securityUtils.getUserName();
        if (!connectedUser.equals(owner)) {
            log.debug("Access denied for user " + connectedUser + " to resume with id " + id + " created by " + owner);
            throw new AccessDeniedException(connectedUser + " does not have access to the resume with id " + id);
        }
    }

    private void removePicture(Resume resume) {
        if (resume.getPicture() != null) {
            fileStorageService.deleteFile(resume.getPicture());
        }
    }
}