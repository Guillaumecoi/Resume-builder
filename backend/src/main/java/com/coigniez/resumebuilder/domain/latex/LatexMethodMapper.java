package com.coigniez.resumebuilder.domain.latex;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.latex.dtos.UpdateLatexMethodRequest;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

@Service
public class LatexMethodMapper
        implements Mapper<LatexMethod, CreateLatexMethodRequest, UpdateLatexMethodRequest, LatexMethodResponse> {

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
            "methodType", MethodType.COMMAND);

    @Override
    public LatexMethod toEntity(CreateLatexMethodRequest request) {
        if (request == null) {
            return null;
        }

        // Set default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        return LatexMethod.builder()
                .type(request.getType())
                .name(request.getName())
                .methodType(request.getMethodType())
                .method(request.getMethod())
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
                .methodType(entity.getMethodType())
                .method(entity.getMethod())
                .build();
    }

    @Override
    public void updateEntity(LatexMethod entity, UpdateLatexMethodRequest request) {
        if (request == null) {
            return;
        }

        entity.setName(request.getName());
        entity.setMethod(request.getMethod());
    }

}
