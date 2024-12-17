package com.coigniez.resumebuilder.validation;

import com.coigniez.resumebuilder.interfaces.ObjectHasID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasIDValidator implements ConstraintValidator<HasID, ObjectHasID> {

    @Override
    public boolean isValid(ObjectHasID value, ConstraintValidatorContext context) {
        return value != null && value.getId() != null;
    }
}