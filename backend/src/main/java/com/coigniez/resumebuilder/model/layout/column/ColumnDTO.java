package com.coigniez.resumebuilder.model.layout.column;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.model.layout.enums.ColorLocation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;


@Data
@Builder
public class ColumnDTO {

    private Long id;

    @Min(1) @Max(2)
    private Integer columnNumber;

    @Builder.Default
    private List<ColumnSectionMappingDTO> sectionMappings = new ArrayList<>();

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
    private Double paddingTop = 10.0;
    @Min(0)
    @Builder.Default
    private Double paddingBottom = 10.0;

    private Long layoutId;
}
