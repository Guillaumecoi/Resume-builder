package com.coigniez.resumebuilder.domain.columnsection;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.dtos.UpdateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

@Service
public class ColumnSectionMapper implements Mapper<ColumnSection, CreateColumnSectionRequest, UpdateColumnSectionRequest, ColumnSectionResponse> {

    @Autowired
    private SectionMapper sectionMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
        "itemsep", 8.0,
        "endsep", 20.0
    );

    @Override
    public ColumnSection toEntity(CreateColumnSectionRequest request) {
        if(request == null) {
            return null;
        }

        // Set default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);
        
        return ColumnSection.builder()
            .sectionOrder(request.getSectionOrder())
            .itemsep(request.getItemsep())
            .endsep(request.getEndsep())
            .build();
    }

    @Override
    public ColumnSectionResponse toDto(ColumnSection entity) {
        if(entity == null) {
            return null;
        }
        return ColumnSectionResponse.builder()
            .id(entity.getId())
            .section(sectionMapper.toDto(entity.getSection()))
            .sectionOrder(entity.getSectionOrder())
            .itemsep(entity.getItemsep())
            .endsep(entity.getEndsep())
            .build();
    }

    @Override
    public void updateEntity(ColumnSection entity, UpdateColumnSectionRequest request) {
        if(request == null) {
            return;
        }

        entity.setSectionOrder(request.getSectionOrder());
        entity.setItemsep(request.getItemsep());
        entity.setEndsep(request.getEndsep());
    }
    
}
