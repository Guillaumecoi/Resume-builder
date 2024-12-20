package com.coigniez.resumebuilder.domain.column;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
public class ColumnResponse {

    @NotNull
    private long id;
    @NotNull
    private int columnNumber;
    @NotNull
    private List<ColumnSectionResponse> sectionMappings;

    @NotNull
    private ColorLocation backgroundColor;
    @NotNull
    private ColorLocation textColor;
    @NotNull
    private ColorLocation borderColor;

    @NotNull
    private double paddingLeft;
    @NotNull
    private double paddingRight;
    @NotNull
    private double paddingTop;
    @NotNull
    private double paddingBottom;

    @NotNull
    private double borderLeft;
    @NotNull
    private double borderRight;
    @NotNull
    private double borderTop;
    @NotNull
    private double borderBottom;
}
