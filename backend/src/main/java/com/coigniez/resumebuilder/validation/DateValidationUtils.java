package com.coigniez.resumebuilder.validation;

import java.time.LocalDate;

public class DateValidationUtils {
    
    /**
     * Validates that start date is before end date when both are present
     * @param startDate The start date to validate
     * @param endDate The end date to validate
     * @return true if dates are valid, false otherwise
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return true;
        }
        return startDate.isBefore(endDate);
    }
}