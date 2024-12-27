package com.coigniez.resumebuilder.domain.layoutsectionItem.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLayoutSectionItemRequest implements CreateRequest {

    @NotNull
    private long columnSectionId;
    @NotNull
    private long sectionItemId;
    @NotNull
    private long latexMethodId;

    private boolean hidden;
    private Integer itemOrder;
    private AlignmentType alignment;
    
}
