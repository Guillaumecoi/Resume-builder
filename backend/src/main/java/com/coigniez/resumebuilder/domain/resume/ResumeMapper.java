package com.coigniez.resumebuilder.domain.resume;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.file.FileUtils;
import com.coigniez.resumebuilder.interfaces.Mapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ResumeMapper implements Mapper<Resume, ResumeRequest, ResumeDetailResponse> {

    private final SectionMapper sectionMapper;

    public Resume toEntity(ResumeRequest request) {
        if (request == null) {
            return null;
        }

        return Resume.builder()
                .title(request.getTitle())
                .sections(Optional.ofNullable(request.getSections())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(sectionMapper::toEntity)
                        .collect(Collectors.toCollection(HashSet::new)))
                .build();
    }

    public ResumeDetailResponse toDto(Resume entity) {
        if (entity == null) {
            return null;
        }
    
        return ResumeDetailResponse.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .picture(FileUtils.readFileFromLocation(entity.getPicture()))
            .createdDate(entity.getCreatedDate().toString())
            .lastModifiedDate(entity.getLastModifiedDate().toString())
            .sections(Optional.ofNullable(entity.getSections())
                .orElse(Collections.emptySet())
                .stream().map(sectionMapper::toDto).toList())
            .build();
    }

    public ResumeResponse toSimpleDto(Resume entity) {
        if (entity == null) {
            return null;
        }

        return ResumeResponse.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .createdDate(entity.getCreatedDate().toString())
            .lastModifiedDate(entity.getLastModifiedDate().toString())
            .build();
    }

    @Override
    public void updateEntity(Resume entity, ResumeRequest request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
    }
}