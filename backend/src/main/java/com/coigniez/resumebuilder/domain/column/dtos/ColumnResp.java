package com.coigniez.resumebuilder.domain.column.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.enums.BackgroungImage;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResp;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
public class ColumnResp implements Response {

    @NotNull
    private long id;
    @NotNull
    private int columnNumber;
    @NotNull
    private float columnSize;

    @NotNull
    private ColorLocation backgroundColor;
    @NotNull
    private ColorLocation textColor;
    @NotNull
    private ColorLocation borderColor;

    @NotNull
    private float paddingLeft;
    @NotNull
    private float paddingRight;
    @NotNull
    private float paddingTop;
    @NotNull
    private float paddingBottom;

    @NotNull
    private float borderLeft;
    @NotNull
    private float borderRight;
    @NotNull
    private float borderTop;
    @NotNull
    private float borderBottom;

    @NotNull
    private BackgroungImage backgroundImage;

    @NotNull
    private List<ColumnSectionResp> sectionMappings;
}
