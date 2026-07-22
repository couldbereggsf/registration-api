package com.reggs.registration.web.advice;

import com.reggs.registration.common.exception.EmailAlreadyExistsException;
import com.reggs.registration.common.exception.UsernameAlreadyExistsException;
import com.reggs.registration.common.response.APIResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
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
    public ResponseEntity<APIResponse> handleValidation(MethodArgumentNotValidException ex) {

        // 1. Get all validation errors from the binding result
        String errorMsg = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage()) // or method reference
                .collect(Collectors.joining("; "));
        // If no messages (shouldn't happen), provide a default
        if (errorMsg.isEmpty()) {
            errorMsg = "Validation failed";
        }

        // 2. Build the API response.
        // Assuming my APIResponse constructor is: (Object data, String error, String message, Instant timestamp)
        // Based on my JSON output: {"data":null,"error":"...","message":null,"timestamp":"..."}
        APIResponse response = new APIResponse(null, errorMsg, null, Instant.now());

        // 3. Return 400 Bad Request
        ResponseEntity<APIResponse> body = ResponseEntity.badRequest().body(APIResponse.failure(errorMsg));
        return body;
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
