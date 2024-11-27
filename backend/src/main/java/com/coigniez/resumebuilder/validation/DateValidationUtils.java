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
        // The dates are always valid if the end date is not set
        if (endDate == null) {
            return true;
        }
        // Start date cannot be null if end date is set 
        if (startDate == null) {
            return false;
        }
        // Start date must be before end date
        return startDate.isBefore(endDate);
    }
}