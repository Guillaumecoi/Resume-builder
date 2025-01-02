package com.coigniez.resumebuilder.domain.layoutsubsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemResp;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResp;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutSubSectionResp implements Response {

    @NotNull
    private long id;
    @NotNull
    private int sectionOrder;
    private AlignmentType alignment;

    @NotNull
    private boolean hidden;
    @NotNull
    private boolean defaultOrder;

    @NotNull
    private SubSectionResp subSection;
    @NotNull
    private LatexMethodResponse latexMethod;

    @NotNull
    private List<LayoutSectionItemResp> items;
}