package com.coigniez.resumebuilder.model.resume.resume;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.model.resume.personaldetails.PersonalDetailsMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ResumeMapper implements Mapper<Resume, ResumeRequest, ResumeResponse> {

    private final PersonalDetailsMapper personalDetailsMapper;

    public Resume toEntity(ResumeRequest resumeRequest) {
        if (resumeRequest == null) {
            return null;
        }

        return Resume.builder()
                .title(resumeRequest.title())
                .build();
    }

    public ResumeResponse toDto(Resume resume) {
        if (resume == null) {
            return null;
        }

        return ResumeResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .personalDetails(personalDetailsMapper.toDto(resume.getPersonalDetails()))
                .createdDate(resume.getCreatedDate().toString())
                .lastModifiedDate(resume.getLastModifiedDate().toString())
                .build();
    }
}