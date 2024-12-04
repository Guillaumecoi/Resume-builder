package com.coigniez.resumebuilder.domain.column;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;

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
