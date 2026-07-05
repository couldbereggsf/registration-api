package com.reggs.registration.web.controller;

import com.reggs.registration.common.response.APIResponse;
import com.reggs.registration.service.RegisteredUser;
import com.reggs.registration.service.RegistrationCommand;
import com.reggs.registration.service.RegistrationService;
import com.reggs.registration.web.dto.RegisteredUserResponse;
import com.reggs.registration.web.dto.RegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Deliberately thin, per the JBE "keep controllers clean" principle:
 * validate (declaratively, via the annotation below), delegate to the
 * service, map the result to a response. No business logic lives here.
 */
@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse<RegisteredUserResponse>> register(
            @Validated(RegistrationRequest.OrderedValidation.class) @Valid @RequestBody RegistrationRequest request
    ) {
        RegistrationCommand command = new RegistrationCommand(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getAge()
        );

        RegisteredUser registered = registrationService.register(command);

        RegisteredUserResponse body = new RegisteredUserResponse(
                registered.id(),
                registered.username(),
                registered.email(),
                registered.createdAt()
        );

        // 201 Created + Location header, per the JBE Episode 1 discussion
        // on returning a URI to the newly created resource via UriComponentsBuilder.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registered.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(APIResponse.success(body, "User registered successfully"));
    }
}
