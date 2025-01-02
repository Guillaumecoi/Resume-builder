package com.coigniez.resumebuilder.domain.layoutsectionItem;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemResp;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemSimpleCreateReq;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemUpdateReq;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutSectionItemMapper implements Mapper<LayoutSectionItem, LayoutSectionItemSimpleCreateReq, LayoutSectionItemUpdateReq, LayoutSectionItemResp> {

    private final SectionItemMapper sectionItemMapper;
    private final LatexMethodMapper latexMethodMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
            "hidden", false);

    @Override
    public LayoutSectionItem toEntity(LayoutSectionItemSimpleCreateReq request) {
        if (request == null) {
            return null;
        }

        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        return LayoutSectionItem.builder()
                .hidden(request.getHidden())
                .itemOrder(request.getItemOrder())
                .alignment(request.getAlignment())
                .build();
    }

    @Override
    public LayoutSectionItemResp toDto(LayoutSectionItem entity) {
        if (entity == null) {
            return null;
        }

        return LayoutSectionItemResp.builder()
                .id(entity.getId())
                .latexMethod(latexMethodMapper.toDto(entity.getLatexMethod()))
                .sectionItem(sectionItemMapper.toDto(entity.getSectionItem()))
                .hidden(entity.isHidden())
                .itemOrder(entity.getItemOrder())
                .alignment(entity.getAlignment())
                .build();
    }

    @Override
    public void updateEntity(LayoutSectionItem entity, @Valid LayoutSectionItemUpdateReq request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setHidden(request.isHidden());
        entity.setItemOrder(request.getItemOrder());
        entity.setAlignment(request.getAlignment());
    }
    
}
