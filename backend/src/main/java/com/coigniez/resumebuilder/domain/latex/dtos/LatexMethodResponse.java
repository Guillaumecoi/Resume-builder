package com.coigniez.resumebuilder.domain.latex.dtos;

import com.coigniez.resumebuilder.domain.sectionitem.enums.SectionItemType;
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
    private SectionItemType type;
    @NotNull
    private String name;
    @NotNull
    private String method;
}