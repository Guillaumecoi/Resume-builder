package com.coigniez.resumebuilder.domain.columnsection;

import com.coigniez.resumebuilder.interfaces.ObjectHasID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnSectionRequest implements ObjectHasID {

    private Long id;

    @NotNull
    private Long columnId;
    @NotNull
    private Long sectionId;
    private Integer sectionOrder;
    
    @Builder.Default
    private double itemsep = 8.0;
    @Builder.Default
    private double endsep = 20.0;
}