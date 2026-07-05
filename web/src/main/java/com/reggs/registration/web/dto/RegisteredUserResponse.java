package com.reggs.registration.web.dto;

import java.time.Instant;

public record RegisteredUserResponse(
        Long id,
        String username,
        String email,
        Instant createdAt
) {
}
