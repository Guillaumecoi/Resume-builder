package com.coigniez.resumebuilder.domain.layoutsection;

import org.springframework.beans.factory.annotation.Autowired;

import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.layoutsection.dtos.LayoutSectionResp;
import com.coigniez.resumebuilder.domain.layoutsection.dtos.LayoutSectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.layoutsection.dtos.LayoutSectionUpdateReq;
import com.coigniez.resumebuilder.domain.section.SectionMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;

import jakarta.validation.Valid;

public class LayoutSectionMapper implements Mapper<LayoutSection, LayoutSectionSimpleCreateReq, LayoutSectionUpdateReq, LayoutSectionResp> {

    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private LatexMethodMapper latexMethodMapper;

    @Override
    public LayoutSection toEntity(@Valid LayoutSectionSimpleCreateReq request) {
        return LayoutSection.builder().build();
    }

    @Override
    public LayoutSectionResp toDto(LayoutSection entity) {
        if (entity == null) {
            return null;
        }

        return LayoutSectionResp.builder()
                .section(sectionMapper.toDto(entity.getSection()))
                .latexMethod(latexMethodMapper.toDto(entity.getLatexMethod()))
                .build();
    }

    @Override
    public void updateEntity(LayoutSection entity, @Valid LayoutSectionUpdateReq request) {
        return;
    }
}
    
