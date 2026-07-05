package com.reggs.registration.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that password == confirmPassword on any class implementing
 * {@link PasswordFieldsAware}. No database access needed, so this lives
 * entirely in the framework-light common module.
 */
public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, PasswordFieldsAware> {

    @Override
    public boolean isValid(PasswordFieldsAware dto, ConstraintValidatorContext context) {
        if (dto == null) {
            // Let @NotNull-type constraints on the object itself handle this;
            // a class-level validator should not declare an opinion on null.
            return true;
        }

        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        // Both being blank is not this validator's concern - FirstCheck's
        // @NotBlank already would have caught that and stopped the sequence
        // before we ever get here. We only compare actual values.
        if (password == null || confirmPassword == null) {
            return false;
        }

        return password.equals(confirmPassword);
    }
}
