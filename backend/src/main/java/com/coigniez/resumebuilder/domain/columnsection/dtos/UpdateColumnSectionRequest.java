package com.coigniez.resumebuilder.domain.columnsection.dtos;

import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateColumnSectionRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private Integer sectionOrder;
    @NotNull
    private Double itemsep;
    @NotNull
    private Double endsep;
}