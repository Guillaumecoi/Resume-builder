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
    private ColorLocation borderColor;

    private double paddingLeft;
    private double paddingRight;
    private double paddingTop;
    private double paddingBottom;

    private double borderLeft;
    private double borderRight;
    private double borderTop;
    private double borderBottom;
}
