package com.coigniez.resumebuilder.model.resume.resume;

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
                .email(resumeRequest.email())
                .phone(resumeRequest.phone())
                .address(resumeRequest.address())
                .website(resumeRequest.website())
                .linkedIn(resumeRequest.linkedIn())
                .github(resumeRequest.github())
                .instagram(resumeRequest.instagram())
                .facebook(resumeRequest.facebook())
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
                .email(resume.getEmail())
                .phone(resume.getPhone())
                .address(resume.getAddress())
                .website(resume.getWebsite())
                .linkedIn(resume.getLinkedIn())
                .github(resume.getGithub())
                .instagram(resume.getInstagram())
                .facebook(resume.getFacebook())
                .createdDate(resume.getCreatedDate().toString())
                .lastModifiedDate(resume.getLastModifiedDate().toString())
                .build();
    }
}