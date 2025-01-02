package com.coigniez.resumebuilder.domain.layoutsectionItem.dtos;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResp;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutSectionItemResp implements Response {

    @NotNull
    private long id;
    
    @NotNull
    private SectionItemResp sectionItem;
    @NotNull
    private LatexMethodResponse latexMethod;

    @NotNull
    private boolean hidden;
    @NotNull
    private Integer itemOrder;
    private AlignmentType alignment;
    
}
