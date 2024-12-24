package com.coigniez.resumebuilder.domain.latex.dtos;


import com.coigniez.resumebuilder.domain.latex.HasLatexMethod;
import com.coigniez.resumebuilder.domain.latex.MethodType;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLatexMethodRequest implements CreateRequest {

    @NotNull
    private long layoutId;

    @NotNull
    private HasLatexMethod type;
    @NotBlank
    private String name;
    private MethodType methodType;
    @NotBlank
    private String method;
}
