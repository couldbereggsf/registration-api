package com.reggs.registration.common.validation;

/**
 * Validation group marker for structural / cross-field checks that only
 * make sense once FirstCheck has already passed - e.g. "does password
 * equal confirmPassword?" or "is the user at least 18?"
 *
 * These are still in-memory checks (no database hit), but they are more
 * expensive than a simple @NotBlank, and some of them are meaningless to
 * run against a blank field. Hence they run second, never first.
 */
public interface SecondCheck {
}
