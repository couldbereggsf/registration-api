package com.reggs.registration.common.validation;

/**
 * Any DTO annotated with {@link PasswordsMatch} must implement this so the
 * validator can compare the two fields directly, instead of reaching for
 * reflection or hardcoded field names (which would silently break if a
 * field gets renamed).
 */
public interface PasswordFieldsAware {

    String getPassword();

    String getConfirmPassword();
}
