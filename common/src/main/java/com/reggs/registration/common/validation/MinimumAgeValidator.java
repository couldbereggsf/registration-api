package com.reggs.registration.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, Integer> {

    private int minimumAge;

    @Override
    public void initialize(MinimumAge constraintAnnotation) {
        this.minimumAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext context) {
        // Null is intentionally NOT this validator's concern - @NotNull
        // in FirstCheck already would have stopped the sequence before
        // this validator ever runs.
        if (age == null) {
            return true;
        }
        return age >= minimumAge;
    }
}
