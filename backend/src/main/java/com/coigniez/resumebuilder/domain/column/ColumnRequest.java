package com.coigniez.resumebuilder.domain.column;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRequest;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
public class ColumnRequest {

    private Long id;

    @Min(1) @Max(2)
    private Integer columnNumber;

    @Builder.Default
    @NotNull
    private List<ColumnSectionRequest> sectionMappings = new ArrayList<>();

    private ColorLocation backgroundColor;
    private ColorLocation textColor;

    @Min(0)
    @Builder.Default
    private Double paddingLeft = 10.0;
    @Min(0)
    @Builder.Default
    private Double paddingRight = 10.0;
    @Min(0)
    @Builder.Default
    private Double paddingTop = 20.0;
    @Min(0)
    @Builder.Default
    private Double paddingBottom = 20.0;

    private Long layoutId;
}
