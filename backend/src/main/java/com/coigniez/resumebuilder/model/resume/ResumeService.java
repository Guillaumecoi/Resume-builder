package com.coigniez.resumebuilder.model.resume;

import java.util.List;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeService implements CrudService<ResumeResponse, ResumeRequest> {

    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final FileStorageService fileStorageService;

    public Long create(ResumeRequest request, Authentication user) {
        Resume resume = resumeMapper.toEntity(request);
        return resumeRepository.save(resume).getId();
    }

    public ResumeResponse get(Long id, Authentication connectedUser) {
        hasAccess(id, connectedUser);
        Resume resume = resumeRepository.findById(id).orElseThrow();
        return resumeMapper.toDto(resume);
    }

    public void update(Long id, ResumeRequest request, Authentication connectedUser) {
        hasAccess(id, connectedUser);
        Resume resume = resumeMapper.toEntity(request);
        resume.setId(id);
        resumeRepository.save(resume);
    }

    public void delete(Long id, Authentication connectedUser) {
        hasAccess(id, connectedUser);
        resumeRepository.deleteById(id);
    }

    public PageResponse<ResumeResponse> getAll(int page, int size, String order, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(order).descending());
        Page<Resume> resumes = resumeRepository.findAllByCreatedBy(pageable, connectedUser.getName());
        // Convert the page of resumes to a list of resume responses
        List<ResumeResponse> resumeResponses = resumes.stream()
            .map(resumeMapper::toDto)
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
        String picture = fileStorageService.saveFile(file, connectedUser.getName());
        resume.setPicture(picture);
        resumeRepository.save(resume);
    }

    public void deleteAll(Authentication connectedUser) {
        resumeRepository.deleteAllByCreatedBy(connectedUser.getName());
    }

    /**
     * Check if the connected user has access to the resume
     * 
     * @param id the id of the resume
     * @param connectedUser the connected user
     * @throws AccessDeniedException if the connected user does not have access to the resume
     */
    private void hasAccess(Long id, Authentication connectedUser) throws AccessDeniedException {
        Resume resume = resumeRepository.findById(id).orElseThrow();
        if (!resume.getCreatedBy().equals(connectedUser.getName())) {
            throw new AccessDeniedException(connectedUser.getName() + " does not have access to the resume with id " + id);
        }
    }
}