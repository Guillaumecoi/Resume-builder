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
        Resume resume = resumeRepository.findById(id).orElseThrow();
        if (!resume.getCreatedBy().equals(connectedUser.getName())) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        return resumeMapper.toDto(resume);
    }

    public void update(ResumeRequest request, Authentication connectedUser) {
        Resume resume = resumeMapper.toEntity(request);
        String createdBy = resumeRepository.findById(resume.getId()).orElseThrow().getCreatedBy();
        if (!createdBy.equals(connectedUser.getName())) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        resumeRepository.save(resume);
    }

    public void delete(Long id, Authentication connectedUser) {
        String createdBy = resumeRepository.findById(id).orElseThrow().getCreatedBy();
        if (!createdBy.equals(connectedUser.getName())) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
        resumeRepository.deleteById(id);
    }

}
