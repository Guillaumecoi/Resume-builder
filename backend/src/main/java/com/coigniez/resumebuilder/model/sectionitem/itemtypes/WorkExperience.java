package com.coigniez.resumebuilder.model.sectionitem.itemtypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkExperience {
    private String jobTitle;
    private String companyName;
    private String startDate;
    private String endDate;
    private String description;
}
