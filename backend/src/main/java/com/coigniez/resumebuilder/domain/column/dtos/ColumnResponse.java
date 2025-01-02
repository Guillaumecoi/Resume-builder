package com.coigniez.resumebuilder.domain.column.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResp;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
public class ColumnResponse implements Response {

    @NotNull
    private long id;
    @NotNull
    private int columnNumber;
    @NotNull
    private List<ColumnSectionResp> sectionMappings;

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
