package com.coigniez.resumebuilder.model.layout.column.ColumnSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.model.section.SectionMapper;

@Service
public class ColumnSectionMapper implements Mapper<ColumnSection, ColumnSectionRequest, ColumnSectionResponse> {

    @Autowired
    private SectionMapper sectionMapper;

    @Override
    public ColumnSection toEntity(ColumnSectionRequest request) {
        return ColumnSection.builder()
            .id(request.getId())
            .position(request.getPosition())
            .itemsep(request.getItemsep())
            .build();
    }

    @Override
    public ColumnSectionResponse toDto(ColumnSection entity) {
        return ColumnSectionResponse.builder()
            .id(entity.getId())
            .section(sectionMapper.toDto(entity.getSection()))
            .position(entity.getPosition())
            .itemsep(entity.getItemsep())
            .build();
    }
    
}
