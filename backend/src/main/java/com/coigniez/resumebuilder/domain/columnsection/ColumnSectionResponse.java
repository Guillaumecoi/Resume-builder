package com.coigniez.resumebuilder.domain.columnsection;

import com.coigniez.resumebuilder.domain.section.SectionResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnSectionResponse {

    private Long id;
    private SectionResponse section;
    private Integer position;
    private double itemsep;
}