package com.coigniez.resumebuilder.model.resume;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.common.PageResponse;

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

    public Long create(Long parentId, ResumeRequest request, Authentication user) {
        Resume resume = resumeMapper.toEntity(request);
        return resumeRepository.save(resume).getId();
    }

    public ResumeDetailResponse get(Long id, Authentication connectedUser) {
        hasAccess(id, connectedUser);
        Resume resume = resumeRepository.findById(id).orElseThrow();
        log.debug("retrieved from repository: " + resume.toString());
        return resumeMapper.toDto(resume);
    }

    public void update(Long id, ResumeRequest request, Authentication connectedUser) {
        hasAccess(id, connectedUser);
        Resume existingResume = resumeRepository.findById(id).orElseThrow();
        Resume updatedResume = resumeMapper.toEntity(request);
        // Copy the properties from the updated resume to the existing resume
        BeanUtils.copyProperties(updatedResume, existingResume, "id", "createdBy", "createdDate", "lastModifiedDate", "sections", "picture");
    
        resumeRepository.save(existingResume);
    }

    public void delete(Long id, Authentication connectedUser) {
        hasAccess(id, connectedUser);
        try {
            removePicture(resumeRepository.findById(id).orElseThrow());
            resumeRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        }
    }
        

    public PageResponse<ResumeResponse> getAll(int page, int size, String order, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(order).descending());
        Page<Resume> resumes = resumeRepository.findAllByCreatedBy(pageable, connectedUser.getName());
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
 
    public void uploadPicture(Long id, MultipartFile file, Authentication connectedUser) {
        hasAccess(id, connectedUser);
        Resume resume = resumeRepository.findById(id).orElseThrow();
        removePicture(resume);
        log.debug(null == file ? "null file" : "file name: " + file.getOriginalFilename());
        String picture = fileStorageService.saveFile(file, connectedUser.getName());
        resume.setPicture(picture);
        resumeRepository.save(resume);
        log.debug(picture + " saved to resume with id " + id);
    }

    public void deleteAll(Authentication connectedUser) {
        try {
            List<Resume> resumes = resumeRepository.findAllByCreatedBy(connectedUser.getName());
            for (Resume resume : resumes) {
                removePicture(resume);
            }
            resumeRepository.deleteAllByCreatedBy(connectedUser.getName());
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
    public void hasAccess(Long id, Authentication connectedUser) throws AccessDeniedException {
        Resume resume = resumeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Resume with id " + id + " not found"));
        if (!connectedUser.getName().equals(resume.getCreatedBy())) {
            log.debug("Access denied for user " + connectedUser.getName() + " to resume with id " + id + " created by " + resume.getCreatedBy());
            throw new AccessDeniedException(connectedUser.getName() + " does not have access to the resume with id " + id);
        }
    }

    private void removePicture(Resume resume) {
        if (resume.getPicture() != null) {
            fileStorageService.deleteFile(resume.getPicture());
        }
    }
}