package com.coigniez.resumebuilder.domain.column.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.enums.BackgroungImage;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ColumnSimpleCreateReq implements CreateRequest {

    @Min(1)
    @Max(10)
    private Short columnNumber;

    @DecimalMin("0.1")
    @DecimalMax("5.0")
    private Float columnSize;

    private ColorLocation backgroundColor;
    private ColorLocation textColor;
    private ColorLocation borderColor;

    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float paddingLeft;
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float paddingRight;
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float paddingTop;
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float paddingBottom;

    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float borderLeft;
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float borderRight;
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float borderTop;
    @DecimalMin("0.0")
    @DecimalMax("999.9")
    private Float borderBottom;

    private BackgroungImage backgroundImage;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnSectionSimpleCreateReq> sectionMappings;
}
