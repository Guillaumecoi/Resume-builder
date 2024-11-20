package com.coigniez.resumebuilder.model.resume.resume;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.coigniez.resumebuilder.interfaces.CrudService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeService implements CrudService<ResumeResponse, ResumeRequest> {

    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;

    public Long create(ResumeRequest request, Authentication user) {
        Resume resume = resumeMapper.toEntity(request);
        return resumeRepository.save(resume).getId();
    }

    public ResumeResponse get(Long id, Authentication connectedUser) {
        if (!hasAccess(id, connectedUser)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        Resume resume = resumeRepository.findById(id).orElseThrow();
        return resumeMapper.toDto(resume);
    }

    public void update(Long id, ResumeRequest request, Authentication connectedUser) {
        if (!hasAccess(id, connectedUser)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        Resume resume = resumeMapper.toEntity(request);
        resumeRepository.save(resume);
    }

    public void delete(Long id, Authentication connectedUser) {
        if (!hasAccess(id, connectedUser)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        resumeRepository.deleteById(id);
    }

    /**
     * Check if the connected user has access to the resume
     * 
     * @param id the id of the resume
     * @param connectedUser the connected user
     * @return true if the connected user has access to the resume, false otherwise
     */
    private boolean hasAccess(Long id, Authentication connectedUser) {
        Resume resume = resumeRepository.findById(id).orElseThrow();
        return resume.getCreatedBy().equals(connectedUser.getName());
    }
}