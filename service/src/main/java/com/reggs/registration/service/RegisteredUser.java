package com.reggs.registration.service;

import java.time.Instant;

/**
 * What the service hands back after a successful registration. Notice
 * there is no password or passwordHash field here at all, because the result
 * the controller sees should never be able to leak it, and this is by construction.
 */
public record RegisteredUser(
        Long id,
        String username,
        String email,
        Instant createdAt
) {
}
