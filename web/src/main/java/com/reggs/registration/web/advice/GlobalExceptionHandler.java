package com.reggs.registration.web.advice;

import com.reggs.registration.common.exception.EmailAlreadyExistsException;
import com.reggs.registration.common.exception.UsernameAlreadyExistsException;
import com.reggs.registration.common.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * The AOP-style cross-cutting piece from JBE Episode 3: intercepts
 * exceptions thrown anywhere in any controller, so individual endpoints
 * never need their own try/catch.
 *
 * Important nuance carried over from my JBE notes: validation failures
 * triggered by @Valid on a @RequestBody arrive as
 * MethodArgumentNotValidException - NOT ConstraintViolationException.
 * The latter is what I'd get from validating method parameters or
 * class-level constraints outside of @RequestBody. They are different
 * exception types with no convenient shared validation superclass.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Fires whenever ANY constraint group in RegistrationRequest's
     * @GroupSequence fails - whichever group failed first. Because of
     * fail-fast group ordering, this will only ever contain errors from
     * the single group that stopped the sequence (see the GroupSequence
     * concerns section of my JBE study guide).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidationFailure(MethodArgumentNotValidException ex) {
        String combinedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .badRequest()
                .body(APIResponse.failure(combinedMessage));
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<APIResponse<Void>> handleUsernameTaken(UsernameAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(APIResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<APIResponse<Void>> handleEmailTaken(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(APIResponse.failure(ex.getMessage()));
    }

    /**
     * Catch-all safety net. Deliberately never exposes ex.getMessage()
     * here, because that could leak DB or implementation details, exactly the
     * concern raised in my JBE notes about error message design.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleUnexpected(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.failure("Something went wrong. Please try again."));
    }
}
