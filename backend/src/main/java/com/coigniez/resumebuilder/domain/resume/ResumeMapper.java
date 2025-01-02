package com.coigniez.resumebuilder.domain.resume;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.resume.dtos.*;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.file.FileUtils;
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

        return Resume.builder()
                .title(request.getTitle())
                .sections(request.getSections().stream().map(sectionMapper::toEntity).collect(Collectors.toSet()))
                .build();
    }

    public ResumeResp toDto(Resume entity) {
        if (entity == null) {
            return null;
        }
    
        return ResumeResp.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .picture(FileUtils.readFileFromLocation(entity.getPicture()))
            .createdDate(entity.getCreatedDate().toString())
            .lastModifiedDate(entity.getLastModifiedDate().toString())
            .sections(entity.getSections().stream().map(sectionMapper::toDto).collect(Collectors.toList()))
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