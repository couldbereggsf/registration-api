package com.reggs.registration.service;

/**
 * The plain input the service layer accepts. Deliberately NOT the same
 * class as the web module's validated DTO - the service layer shouldn't
 * know about Jakarta Validation annotations or @GroupSequence at all.
 * By the time this reaches the service, validation has already happened
 * in the web layer.
 */
public record RegistrationCommand(
        String username,
        String email,
        String password,
        Integer age
) {
}
