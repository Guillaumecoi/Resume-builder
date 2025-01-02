package com.coigniez.resumebuilder.domain.layoutsectionItem.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LayoutSectionItemSimpleCreateReq implements CreateRequest {

    @NotNull
    private long sectionItemId;
    private Long latexMethodId;

    private Boolean hidden;
    private Integer itemOrder;
    private AlignmentType alignment;
    
}
