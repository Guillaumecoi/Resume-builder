package com.coigniez.resumebuilder.domain.latex.dtos;

import com.coigniez.resumebuilder.domain.latex.HasLatexMethod;
import com.coigniez.resumebuilder.domain.latex.MethodType;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatexMethodResponse implements Response {
    
    @NotNull
    private long id;
    @NotNull
    private HasLatexMethod type;
    @NotNull
    private String name;
    @NotNull
    private MethodType methodType;
    @NotNull
    private String method;
}