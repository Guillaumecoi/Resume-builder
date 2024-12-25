package com.coigniez.resumebuilder.domain.columnsection.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateColumnSectionRequest implements CreateRequest {

    @NotNull
    private long columnId;
    @NotNull
    private long sectionId;
    @NotNull
    private long latexMethodId;

    private Integer sectionOrder;
    private Double itemsep;
    private Double endsep;
    private AlignmentType alignment;

    private boolean hidden;
    private boolean defaultOrder;

}