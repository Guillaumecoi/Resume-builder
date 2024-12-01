package com.coigniez.resumebuilder.model.layout.column;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnSectionMappingDTO {

    private Long id;

    @NotNull
    private Long columnId;
    @NotNull
    private Long sectionId;
    @NotNull
    private Integer position;
    
    @Builder.Default
    private double itemsep = 8.0;
}