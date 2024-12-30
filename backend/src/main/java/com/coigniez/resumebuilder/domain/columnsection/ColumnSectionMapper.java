package com.coigniez.resumebuilder.domain.columnsection;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.dtos.UpdateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

@Service
public class ColumnSectionMapper implements Mapper<ColumnSection, CreateColumnSectionRequest, UpdateColumnSectionRequest, ColumnSectionResponse> {

    @Autowired
    private LatexMethodMapper latexMethodMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
        "itemsep", 8.0,
        "endsep", 20.0,
        "hidden", false,
        "defaultOrder", true
    );

    @Override
    public ColumnSection toEntity(CreateColumnSectionRequest request) {
        if(request == null) {
            return null;
        }

        // Set default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);
        
        return ColumnSection.builder()
            .itemOrder(request.getItemOrder())
            .itemsep(request.getItemsep())
            .endsep(request.getEndsep())
            .alignment(request.getAlignment())
            .hidden(request.getHidden())
            .defaultOrder(request.getDefaultOrder())
            .build();
    }

    @Override
    public ColumnSectionResponse toDto(ColumnSection entity) {
        if(entity == null) {
            return null;
        }

        // Todo layoutSection
        return ColumnSectionResponse.builder()
            .id(entity.getId())
            .latexMethod(latexMethodMapper.toDto(entity.getLatexMethod()))
            .itemOrder(entity.getItemOrder())
            .itemsep(entity.getItemsep())
            .endsep(entity.getEndsep())
            .alignment(entity.getAlignment())
            .hidden(entity.isHidden())
            .defaultOrder(entity.isDefaultOrder())
            .build();
    }

    @Override
    public void updateEntity(ColumnSection entity, UpdateColumnSectionRequest request) {
        if(request == null) {
            return;
        }

        entity.setItemOrder(request.getItemOrder());
        entity.setItemsep(request.getItemsep());
        entity.setEndsep(request.getEndsep());
        entity.setAlignment(request.getAlignment());
        entity.setHidden(request.isHidden());
        entity.setDefaultOrder(request.isDefaultOrder());
    }
    
}
