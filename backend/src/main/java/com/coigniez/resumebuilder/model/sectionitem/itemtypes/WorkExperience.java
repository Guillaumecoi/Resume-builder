package com.coigniez.resumebuilder.model.sectionitem.itemtypes;

import java.time.LocalDate;
import java.util.List;

import com.coigniez.resumebuilder.validation.DateValidationUtils;

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
    private List<String> responsibilities;

    @AssertTrue
    public boolean isValidDates() {
        return DateValidationUtils.isValidDateRange(startDate, endDate);
    }
}
