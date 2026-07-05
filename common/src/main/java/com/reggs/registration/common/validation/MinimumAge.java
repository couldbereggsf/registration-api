package com.reggs.registration.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Confirms an Integer age field meets a minimum threshold. Field-level
 * (not class-level), but still belongs in SecondCheck rather than
 * FirstCheck: a blank/null age should be caught by @NotNull first, and
 * it's pointless to evaluate "is null >= 18" - that's the kind of
 * unnecessary evaluation @GroupSequence is meant to prevent.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumAgeValidator.class)
public @interface MinimumAge {

    int value() default 18;

    String message() default "Must be at least {value} years old";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
