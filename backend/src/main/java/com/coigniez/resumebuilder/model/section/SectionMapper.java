package com.coigniez.resumebuilder.model.section;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.Mapper;

@Service
public class SectionMapper implements Mapper<Section, SectionRequest, SectionResponse> {

    @Override
    public Section toEntity(SectionRequest request) {
        if (request == null) {
            return null;
        }

        return Section.builder()
                .title(request.title())
                .build();
    }

    @Override
    public SectionResponse toDto(Section entity) {
        if (entity == null) {
            return null;
        }

        return SectionResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .build();
    }




    
}
