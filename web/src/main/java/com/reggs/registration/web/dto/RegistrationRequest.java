package com.reggs.registration.web.dto;

import com.reggs.registration.common.validation.FirstCheck;
import com.reggs.registration.common.validation.MinimumAge;
import com.reggs.registration.common.validation.PasswordFieldsAware;
import com.reggs.registration.common.validation.PasswordsMatch;
import com.reggs.registration.common.validation.SecondCheck;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The DTO from Project 1 in the JBE study guide.
 *
 * Validation runs in three explicit stages via
 * {@link RegistrationRequest.OrderedValidation}:
 *
 *   Stage 1 (FirstCheck)  - cheap, in-memory: are fields present and well-formed?
 *   Stage 2 (SecondCheck) - in-memory, but cross-field: passwords match, age >= 18
 *   Stage 3 (Default)     - the implicit group this class itself belongs to.
 *                           No DB-backed constraints are attached directly to
 *                           this DTO (see RegistrationService for that check) -
 *                           Default exists here only as the required final
 *                           link in the @GroupSequence chain. Per the JBE
 *                           "concerns" notes: forgetting to include the
 *                           class's own Default group in the sequence is an
 *                           easy way to silently skip un-grouped constraints.
 */
@PasswordsMatch(groups = SecondCheck.class)
public class RegistrationRequest implements PasswordFieldsAware {

    @NotBlank(message = "Username is required", groups = FirstCheck.class)
    private String username;

    @NotBlank(message = "Email is required", groups = FirstCheck.class)
    @Email(message = "Email must be a valid email address", groups = FirstCheck.class)
    private String email;

    @NotBlank(message = "Password is required", groups = FirstCheck.class)
    private String password;

    @NotBlank(message = "Confirm password is required", groups = FirstCheck.class)
    private String confirmPassword;

    @NotNull(message = "Age is required", groups = FirstCheck.class)
    @MinimumAge(value = 18, message = "You must be at least 18 years old", groups = SecondCheck.class)
    private Integer age;

    public RegistrationRequest() {
        // required for Jackson deserialization
    }

    public RegistrationRequest(String username, String email, String password, String confirmPassword, Integer age) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public Integer getAge() {
        return age;
    }

    /**
     * The ordering contract: FirstCheck must fully pass before SecondCheck
     * is even attempted; SecondCheck must fully pass before this class's
     * own Default-group constraints (currently none) are attempted.
     *
     * Reminder from the JBE concerns notes: a blank-everything payload
     * will NEVER reach SecondCheck, so @PasswordsMatch will never run in
     * that scenario - tests must use a "valid shape, invalid semantics"
     * payload to actually exercise it. See RegistrationControllerTest.
     */
    @GroupSequence({FirstCheck.class, SecondCheck.class, RegistrationRequest.class})
    public interface OrderedValidation {
    }
}
