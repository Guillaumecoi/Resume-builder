package com.coigniez.resumebuilder.model.section.sectionitem.itemtypes;

import java.time.LocalDate;

import com.coigniez.resumebuilder.validation.DateValidationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkExperience implements SectionItemData {
    @NotBlank
    private String jobTitle;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String responsibilities;  // Items are seperated by the newline character '\n'

    @JsonIgnore
    @AssertTrue
    public boolean isValidDates() {
        return DateValidationUtils.isValidDateRange(startDate, endDate);
    }
}
