package com.coigniez.resumebuilder.domain.layoutsectionrow.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layoutsubsection.dtos.LayoutSubSectionResp;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutSectionRowResp implements Response {

    @NotNull
    private long id;

    @NotNull
    private int rowOrder;

    @NotNull
    private LatexMethodResponse latexMethod;
    @NotNull
    private List<LayoutSubSectionResp> subSections;
}
