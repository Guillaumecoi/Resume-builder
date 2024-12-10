package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Picture implements SectionItemData {

    private String path;  
    private String caption;

    @Builder.Default
    private boolean center = true;

    @Builder.Default
    @DecimalMin("0.05") @DecimalMax("1.0")
    private Double width = 0.9;
    @Builder.Default
    private Double height = 1.1;

    @Builder.Default
    private Integer rounded = 60;

    @Builder.Default
    private Double zoom = 1.0;

    @Builder.Default
    private Double xoffset = 0.0;
    @Builder.Default
    private Double yoffset = 0.0;

    @Builder.Default
    @Min(0) @Max(5)
    private Double shadow = 0.0;
}