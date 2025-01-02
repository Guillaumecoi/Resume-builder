package com.coigniez.resumebuilder.domain.layoutsectionItem.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutSectionItemUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long sectionItemId;
    @NotNull
    private Long latexMethodId;

    @NotNull
    private boolean hidden;
    private Integer itemOrder;
    private AlignmentType alignment;
    
}