package com.reggs.registration.common.exception;

/**
 * Thrown by the service layer when the Default-group uniqueness check
 * (the expensive, DB-hitting validation step) finds the username already
 * taken. Deliberately a plain RuntimeException with a safe, user-facing
 * message - never wraps or exposes the underlying DB exception.
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' is already taken");
    }
}
