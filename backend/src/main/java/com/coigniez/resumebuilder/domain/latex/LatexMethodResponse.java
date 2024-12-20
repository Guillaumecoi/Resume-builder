package com.coigniez.resumebuilder.domain.latex;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatexMethodResponse {
    
    @NotNull
    private long id;
    @NotNull
    private SectionItemType type;
    @NotNull
    private String name;
    @NotNull
    private String method;
}