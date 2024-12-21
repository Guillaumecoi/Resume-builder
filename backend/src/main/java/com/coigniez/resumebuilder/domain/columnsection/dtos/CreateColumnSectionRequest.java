package com.coigniez.resumebuilder.domain.columnsection.dtos;

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

    private Integer sectionOrder;
    private Double itemsep;
    private Double endsep;
}