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
public class Education implements SectionItemData {

    @NotBlank
    private String degree;
    @NotBlank
    private String institution;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    @JsonIgnore
    @AssertTrue
    public boolean isValidDates() {
        return DateValidationUtils.isValidDateRange(startDate, endDate);
    }
}

