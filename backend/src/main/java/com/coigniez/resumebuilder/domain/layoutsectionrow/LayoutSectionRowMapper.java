package com.coigniez.resumebuilder.domain.layoutsectionrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.layoutsectionrow.dtos.LayoutSectionRowResp;
import com.coigniez.resumebuilder.domain.layoutsectionrow.dtos.LayoutSectionRowSimpleCreateReq;
import com.coigniez.resumebuilder.domain.layoutsectionrow.dtos.LayoutSectionUpdateReq;
import com.coigniez.resumebuilder.interfaces.Mapper;

import jakarta.validation.Valid;

@Service
public class LayoutSectionRowMapper implements Mapper<LayoutSectionRow, LayoutSectionRowSimpleCreateReq, LayoutSectionUpdateReq, LayoutSectionRowResp> {

    @Autowired
    private LatexMethodMapper latexMethodMapper;


    @Override
    public LayoutSectionRow toEntity(LayoutSectionRowSimpleCreateReq request) {
        if(request == null) {
            return null;
        }
        
        return LayoutSectionRow.builder()
            .rowOrder(request.getRowOrder())
            .build();
    }

    @Override
    public LayoutSectionRowResp toDto(LayoutSectionRow entity) {
        if(entity == null) {
            return null;
        }

        return LayoutSectionRowResp.builder()
            .id(entity.getId())
            .latexMethod(latexMethodMapper.toDto(entity.getLatexMethod()))
            .rowOrder(entity.getRowOrder())
            .build();
    }

    @Override
    public void updateEntity(LayoutSectionRow entity, @Valid LayoutSectionUpdateReq request) {
        if(request == null) {
            return;
        }

        entity.setRowOrder(request.getRowOrder());
    }
    
}
