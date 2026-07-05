package com.reggs.registration.common.response;

import java.time.Instant;

/**
 * The standard response envelope discussed in JBE Episode 1.
 *
 * Every endpoint in this project returns one of these (wrapped in a
 * ResponseEntity so the HTTP status code stays explicit) instead of a
 * plain object. This avoids the "reverse engineering a response entity"
 * problem: clients always know where to look for data, message, and error.
 *
 * @param <T> the type of the payload being returned
 */
public class APIResponse<T> {

    private final T data;
    private final String message;
    private final String error;
    private final Instant timestamp;

    private APIResponse(T data, String message, String error) {
        this.data = data;
        this.message = message;
        this.error = error;
        this.timestamp = Instant.now();
    }

    /** Use for any successful response that carries a payload. */
    public static <T> APIResponse<T> success(T data, String message) {
        return new APIResponse<>(data, message, null);
    }

    /** Use for any successful response with no payload (e.g. logout). */
    public static APIResponse<Void> success(String message) {
        return new APIResponse<>(null, message, null);
    }

    /**
     * Use inside @ExceptionHandler methods. Deliberately takes a plain
     * String, not the raw exception - we never want to leak stack traces
     * or DB error text back to the client (see JBE notes on "user-friendly
     * error message" vs "exposing implementation details").
     */
    public static APIResponse<Void> failure(String error) {
        return new APIResponse<>(null, null, error);
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
