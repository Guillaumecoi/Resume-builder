package com.coigniez.resumebuilder.domain.layoutsection.dtos;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutSectionResponse  implements Response {

    @NotNull
    private long id;
    @NotNull
    private SectionResp section;
    @NotNull
    private LatexMethodResponse latexMethod;
}