package com.coigniez.resumebuilder.domain.layoutsubsection.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutSubSectionUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long rowId;
    private Long latexMethodId;

    @NotNull
    private Integer sectionOrder;
    private AlignmentType alignment;

    @NotNull
    private Boolean hidden;
    @NotNull
    private Boolean defaultOrder;
    
}
