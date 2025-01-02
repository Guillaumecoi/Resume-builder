package com.coigniez.resumebuilder.domain.columnsection;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResp;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionUpdateReq;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

@Service
public class ColumnSectionMapper implements Mapper<ColumnSection, ColumnSectionSimpleCreateReq, ColumnSectionUpdateReq, ColumnSectionResp> {

    @Autowired
    private LatexMethodMapper latexMethodMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
        "itemsep", 8.0,
        "endsep", 20.0,
        "hidden", false
    );

    @Override
    public ColumnSection toEntity(ColumnSectionSimpleCreateReq request) {
        if(request == null) {
            return null;
        }

        // Set default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);
        
        return ColumnSection.builder()
            .sectionOrder(request.getSectionOrder())
            .itemsep(request.getItemsep())
            .endsep(request.getEndsep())
            .alignment(request.getAlignment())
            .hidden(request.getHidden())
            .build();
    }

    @Override
    public ColumnSectionResp toDto(ColumnSection entity) {
        if(entity == null) {
            return null;
        }

        // Todo layoutSection
        return ColumnSectionResp.builder()
            .id(entity.getId())
            .latexMethod(latexMethodMapper.toDto(entity.getLatexMethod()))
            .sectionOrder(entity.getSectionOrder())
            .itemsep(entity.getItemsep())
            .endsep(entity.getEndsep())
            .alignment(entity.getAlignment())
            .hidden(entity.isHidden())
            .build();
    }

    @Override
    public void updateEntity(ColumnSection entity, ColumnSectionUpdateReq request) {
        if(request == null) {
            return;
        }

        entity.setSectionOrder(request.getSectionOrder());
        entity.setItemsep(request.getItemsep());
        entity.setEndsep(request.getEndsep());
        entity.setAlignment(request.getAlignment());
        entity.setHidden(request.isHidden());
    }
    
}
