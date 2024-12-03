package com.coigniez.resumebuilder.model.layout.column;

import java.util.List;

import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSectionResponse;
import com.coigniez.resumebuilder.model.layout.enums.ColorLocation;

import lombok.*;


@Data
@Builder
public class ColumnResponse {

    private Long id;
    private Integer columnNumber;
    private List<ColumnSectionResponse> sectionMappings;

    private ColorLocation backgroundColor;
    private ColorLocation textColor;

    private Double paddingLeft;
    private Double paddingRight;
    private Double paddingTop;
    private Double paddingBottom;
}
