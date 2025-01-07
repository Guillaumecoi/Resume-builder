package com.coigniez.resumebuilder.domain.resume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.resume.dtos.*;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ResumeMapper implements Mapper<Resume, ResumeCreateReq, ResumeUpdateReq, ResumeResp> {

    private final SectionMapper sectionMapper;

    public Resume toEntity(ResumeCreateReq request) {
        if (request == null) {
            return null;
        }

        Resume resume = Resume.builder()
                .title(request.getTitle())
                .sections(new ArrayList<>())
                .layouts(new ArrayList<>())
                .build();

        Optional.ofNullable(request.getSections())
                .ifPresent(sections -> sections.forEach(section -> {
                    resume.addSection(sectionMapper.toEntity(section));
                }));

        return resume;
    }

    public ResumeResp toDto(Resume entity) {
        if (entity == null) {
            return null;
        }

        return ResumeResp.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .createdDate(entity.getCreatedDate().toString())
                .lastModifiedDate(entity.getLastModifiedDate().toString())
                .sections(Optional.ofNullable(entity.getSections())
                        .map(sections -> sections.stream().map(sectionMapper::toDto).collect(Collectors.toList()))
                        .orElse(Collections.emptyList()))
                .build();
    }

    public ResumeSimpleResp toSimpleDto(Resume entity) {
        if (entity == null) {
            return null;
        }

        return ResumeSimpleResp.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .createdDate(entity.getCreatedDate().toString())
                .lastModifiedDate(entity.getLastModifiedDate().toString())
                .build();
    }

    @Override
    public void updateEntity(Resume entity, ResumeUpdateReq request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
    }
}