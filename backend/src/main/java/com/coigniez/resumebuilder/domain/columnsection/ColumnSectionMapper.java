package com.coigniez.resumebuilder.domain.columnsection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;

@Service
public class ColumnSectionMapper implements Mapper<ColumnSection, ColumnSectionRequest, ColumnSectionResponse> {

    @Autowired
    private SectionMapper sectionMapper;

    @Override
    public ColumnSection toEntity(ColumnSectionRequest request) {
        if(request == null) {
            return null;
        }
        return ColumnSection.builder()
            .id(request.getId())
            .position(request.getPosition())
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
            .position(entity.getPosition())
            .itemsep(entity.getItemsep())
            .endsep(entity.getEndsep())
            .build();
    }

    @Override
    public void updateEntity(ColumnSection entity, ColumnSectionRequest request) {
        if(request == null) {
            return;
        }

        entity.setPosition(request.getPosition());
        entity.setItemsep(request.getItemsep());
        entity.setEndsep(request.getEndsep());
    }
    
}
