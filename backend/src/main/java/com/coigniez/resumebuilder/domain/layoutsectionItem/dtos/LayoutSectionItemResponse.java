package com.coigniez.resumebuilder.domain.layoutsectionItem.dtos;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.interfaces.Response;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayoutSectionItemResponse implements Response {

    @NotNull
    private long id;
    
    @NotNull
    private SectionItemResponse sectionItem;
    @NotNull
    private LatexMethodResponse latexMethod;

    @NotNull
    private boolean hidden;
    @NotNull
    private Integer itemOrder;
    private AlignmentType alignment;
    
}
