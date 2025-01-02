package com.coigniez.resumebuilder.domain.column.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionCreateReq;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
public class CreateColumnRequest implements CreateRequest {

    @NotNull
    private long layoutId;

    @Min(1) @Max(2)
    private Integer columnNumber;

    @NotNull
    private ColorLocation backgroundColor;
    @NotNull
    private ColorLocation textColor;
    @NotNull
    private ColorLocation borderColor;

    @Min(0)
    private Double paddingLeft;
    @Min(0)
    private Double paddingRight;
    @Min(0)
    private Double paddingTop;
    @Min(0)
    private Double paddingBottom;

    @Min(0)
    private Double borderLeft;
    @Min(0)
    private Double borderRight;
    @Min(0)
    private Double borderTop;
    @Min(0)
    private Double borderBottom;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnSectionCreateReq> sectionMappings;
}
