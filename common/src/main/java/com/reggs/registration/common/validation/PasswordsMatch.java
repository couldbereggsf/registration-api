package com.reggs.registration.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level constraint: confirms that {@code password} and
 * {@code confirmPassword} match on whatever class it is applied to.
 *
 * This is the exact "does password match confirmPassword?" example from
 * my JBE study notes - a cross-field check that only makes sense once
 * both fields are confirmed non-blank, which is why it belongs in
 * SecondCheck rather than FirstCheck.
 *
 * Usage: annotate the DTO class itself (not a field), and implement
 * {@link PasswordFieldsAware} so the validator can read both fields
 * without needing reflection or hardcoded getter names.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordsMatchValidator.class)
public @interface PasswordsMatch {

    String message() default "Password and confirm password do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
