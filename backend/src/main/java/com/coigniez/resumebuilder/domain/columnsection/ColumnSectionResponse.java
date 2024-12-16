package com.coigniez.resumebuilder.domain.columnsection;

import com.coigniez.resumebuilder.domain.section.SectionResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnSectionResponse {

    private long id;
    private SectionResponse section;
    private int sectionOrder;
    private double itemsep;
    private double endsep;
}