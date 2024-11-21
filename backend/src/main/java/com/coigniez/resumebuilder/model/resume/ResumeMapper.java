package com.coigniez.resumebuilder.model.resume;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.file.FileUtils;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.model.section.SectionMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ResumeMapper implements Mapper<Resume, ResumeRequest, ResumeResponse> {

    private final SectionMapper sectionMapper;

    public Resume toEntity(ResumeRequest request) {
        if (request == null) {
            return null;
        }

        return Resume.builder()
                .title(request.title())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    public ResumeResponse toDto(Resume entity) {
        if (entity == null) {
            return null;
        }

        return ResumeResponse.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .picture(FileUtils.readFileFromLocation(entity.getPicture()))
            .createdDate(entity.getCreatedDate().toString())
            .lastModifiedDate(entity.getLastModifiedDate().toString())
            .sections(entity.getSections().stream()
                .map(sectionMapper::toDto)
                .toList())
            .build();
    }
}