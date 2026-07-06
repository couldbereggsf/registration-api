package com.reggs.registration.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reggs.registration.persistence.repository.UserRepository;
import com.reggs.registration.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises the full @GroupSequence pipeline through the real HTTP layer.
 *
 * Per the GroupSequence concern about testing: a blank-everything payload
 * will ONLY ever reach FirstCheck, so the second test below deliberately
 * sends a "valid shape, invalid semantics" payload (well-formed fields,
 * but passwords that don't match) to actually exercise SecondCheck's
 * @PasswordsMatch validator. Without that second test, we'd have false
 * confidence that the class-level validator works at all.
 */
@WebMvcTest(controllers = com.reggs.registration.web.controller.RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturn400_whenAllFieldsAreBlank_stage1Only() throws Exception {
        String payload = """
                {
                  "username": "",
                  "email": "",
                  "password": "",
                  "confirmPassword": "",
                  "age": null
                }
                """;

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                // We can't assert the password-mismatch message here -
                // SecondCheck never runs because FirstCheck already failed.
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldReturn400_whenShapeIsValidButPasswordsDoNotMatch_stage2() throws Exception {
        String payload = """
                {
                  "username": "reagan",
                  "email": "reaganfwambaa@gmail.com",
                  "password": "S3cret!",
                  "confirmPassword": "DifferentPassword!",
                  "age": 25
                }
                """;

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(
                        org.hamcrest.Matchers.containsString("do not match")));
    }

    @Test
    void shouldReturn201_whenPayloadIsFullyValid() throws Exception {
        when(registrationService.register(any()))
                .thenReturn(new com.reggs.registration.service.RegisteredUser(
                        1L, "reagan", "reaganfwambaa@gmail.com", java.time.Instant.now()));

        String payload = """
                {
                  "username": "reagan",
                  "email": "reagan@example.com",
                  "password": "S3cret!",
                  "confirmPassword": "S3cret!",
                  "age": 25
                }
                """;

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.username").value("reagan"));
    }
}
