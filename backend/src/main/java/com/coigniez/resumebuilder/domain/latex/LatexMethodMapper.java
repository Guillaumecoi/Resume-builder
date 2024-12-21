package com.coigniez.resumebuilder.domain.latex;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.latex.dtos.UpdateLatexMethodRequest;
import com.coigniez.resumebuilder.interfaces.Mapper;

@Service
public class LatexMethodMapper implements Mapper<LatexMethod, CreateLatexMethodRequest, UpdateLatexMethodRequest, LatexMethodResponse> {

    @Override
    public LatexMethod toEntity(CreateLatexMethodRequest dto) {
        if(dto == null) {
            return null;
        }

        return LatexMethod.builder()
                .type(dto.getType())
                .name(dto.getName())
                .method(dto.getMethod())
                .build();
    }

    @Override
    public LatexMethodResponse toDto(LatexMethod entity) {
        if(entity == null) {
            return null;
        }

        return LatexMethodResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .name(entity.getName())
                .method(entity.getMethod())
                .build();
    }

    @Override
    public void updateEntity(LatexMethod entity, UpdateLatexMethodRequest request) {
       if(request == null) {
           return;
       }

        entity.setName(request.getName());
        entity.setMethod(request.getMethod());
    }
    
}
