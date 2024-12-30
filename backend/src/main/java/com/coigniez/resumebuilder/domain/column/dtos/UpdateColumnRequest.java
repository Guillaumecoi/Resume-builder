package com.coigniez.resumebuilder.domain.column.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class UpdateColumnRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    @Min(1)
    @Max(2)
    private Integer columnNumber;

    @NotNull
    private ColorLocation backgroundColor;
    @NotNull
    private ColorLocation textColor;
    @NotNull
    private ColorLocation borderColor;

    @NotNull
    @Min(0)
    private Double paddingLeft;
    @NotNull
    @Min(0)
    private Double paddingRight;
    @NotNull
    @Min(0)
    private Double paddingTop;
    @NotNull
    @Min(0)
    private Double paddingBottom;

    @NotNull
    @Min(0)
    private Double borderLeft;
    @NotNull
    @Min(0)
    private Double borderRight;
    @NotNull
    @Min(0)
    private Double borderTop;
    @NotNull
    @Min(0)
    private Double borderBottom;
}
