package com.coigniez.resumebuilder.domain.latex.dtos;


import com.coigniez.resumebuilder.domain.latex.HasLatexMethod;
import com.coigniez.resumebuilder.domain.latex.MethodType;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class LatexMethodSimpleCreateReq implements CreateRequest {

    @NotNull
    private HasLatexMethod type;
    @NotBlank
    private String name;
    private MethodType methodType;
    @NotBlank
    private String method;
}
