package com.coigniez.resumebuilder.domain.columnsection;

import com.coigniez.resumebuilder.domain.section.SectionResponse;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnSectionResponse {

    @NotNull
    private long id;
    @NotNull
    private SectionResponse section;
    @NotNull
    private int sectionOrder;
    @NotNull
    private double itemsep;
    @NotNull
    private double endsep;
}