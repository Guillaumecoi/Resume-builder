package com.coigniez.resumebuilder.domain.latex;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.Mapper;

@Service
public class LatexMethodMapper implements Mapper<LatexMethod, LatexMethodRequest, LatexMethodResponse> {

    @Override
    public LatexMethod toEntity(LatexMethodRequest dto) {
        if (dto == null) {
            return null;
        }

        return LatexMethod.builder()
                .id(dto.getId())
                .type(dto.getType())
                .name(dto.getName())
                .method(dto.getMethod())
                .build();
    }

    @Override
    public LatexMethodResponse toDto(LatexMethod entity) {
        if (entity == null) {
            return null;
        }

        return LatexMethodResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .name(entity.getName())
                .method(entity.getMethod())
                .build();
    }
    
}
