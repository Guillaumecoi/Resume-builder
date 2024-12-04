package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;
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
    private boolean shadow = false;

    @Builder.Default
    private Double width = 0.9;
    @Builder.Default
    private Double height = 1.1;

    @Builder.Default
    private Integer rounded = 60;

    @Builder.Default
    private Double zoom = 1.0;

    @Builder.Default
    private Double xOffset = 0.0;
    @Builder.Default
    private Double yOffset = 0.0;
}