package com.coigniez.resumebuilder.domain.sectionitem;

import com.coigniez.resumebuilder.domain.latex.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionItemResponse {

    @NotNull
    private long id;
    @NotNull
    private LatexMethodResponse latexMethod;
    @NotNull
    private int itemOrder;
    private AlignmentType alignment;
    
    @NotNull
    private SectionItemData item;
}