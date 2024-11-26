package com.coigniez.resumebuilder.model.sectionitem.itemtypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    private String degree;
    private String institution;
    private String startDate;
    private String endDate;
    private String description;
}

