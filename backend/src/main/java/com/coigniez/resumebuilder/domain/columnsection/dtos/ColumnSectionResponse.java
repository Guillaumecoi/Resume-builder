package com.coigniez.resumebuilder.domain.columnsection.dtos;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResponse;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnSectionResponse implements Response {

    @NotNull
    private long id;
    @NotNull
    private int sectionOrder;
    @NotNull
    private double itemsep;
    @NotNull
    private double endsep;
    private AlignmentType alignment;

    @NotNull
    private boolean hidden;
    @NotNull
    private boolean defaultOrder;

    @NotNull
    private SectionResponse section;
    @NotNull
    private LatexMethodResponse latexMethod;
}