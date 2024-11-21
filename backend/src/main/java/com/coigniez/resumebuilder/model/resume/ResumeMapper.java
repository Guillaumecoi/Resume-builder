package com.coigniez.resumebuilder.model.resume;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.file.FileUtils;
import com.coigniez.resumebuilder.interfaces.Mapper;

@Service
public class ResumeMapper implements Mapper<Resume, ResumeRequest, ResumeResponse> {

    public Resume toEntity(ResumeRequest resumeRequest) {
        if (resumeRequest == null) {
            return null;
        }

        return Resume.builder()
                .title(resumeRequest.title())
                .firstName(resumeRequest.firstName())
                .lastName(resumeRequest.lastName())
                .build();
    }

    public ResumeResponse toDto(Resume resume) {
        if (resume == null) {
            return null;
        }

        return ResumeResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .firstName(resume.getFirstName())
                .lastName(resume.getLastName())
                .picture(FileUtils.readFileFromLocation(resume.getPicture()))
                .createdDate(resume.getCreatedDate().toString())
                .lastModifiedDate(resume.getLastModifiedDate().toString())
                .build();
    }
}