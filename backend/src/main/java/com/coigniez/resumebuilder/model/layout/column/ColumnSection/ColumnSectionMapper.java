package com.coigniez.resumebuilder.model.layout.column.ColumnSection;

import com.coigniez.resumebuilder.interfaces.Mapper;

public class ColumnSectionMapper implements Mapper<ColumnSection, ColumnSectionDTO, ColumnSectionDTO> {

    @Override
    public ColumnSection toEntity(ColumnSectionDTO request) {
        return ColumnSection.builder()
            .id(request.getId())
            .position(request.getPosition())
            .itemsep(request.getItemsep())
            .build();
    }

    @Override
    public ColumnSectionDTO toDto(ColumnSection entity) {
        return ColumnSectionDTO.builder()
            .id(entity.getId())
            .columnId(entity.getColumn().getId())
            .sectionId(entity.getSection().getId())
            .position(entity.getPosition())
            .itemsep(entity.getItemsep())
            .build();
    }
    
}
