package com.coigniez.resumebuilder.domain.column.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class ColumnUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    @Min(1)
    @Max(10)
    private Short columnNumber;

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("5.0")
    private Float columnSize;

    @NotNull
    private ColorLocation backgroundColor;
    @NotNull
    private ColorLocation textColor;
    @NotNull
    private ColorLocation borderColor;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float paddingLeft;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float paddingRight;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float paddingTop;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float paddingBottom;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float borderLeft;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float borderRight;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float borderTop;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private float borderBottom;
}
