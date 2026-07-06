package com.reggs.registration.service;

import com.reggs.registration.common.exception.EmailAlreadyExistsException;
import com.reggs.registration.common.exception.UsernameAlreadyExistsException;
import com.reggs.registration.persistence.entity.User;
import com.reggs.registration.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Mirrors JBE Episode 3's "we need to mock the actual repository call"
 * approach, therefore, the repository is mocked here, not hit for real, so we can
 * control exactly what existsByUsername/existsByEmail return and assert
 * the service reacts correctly in both the happy path and each failure
 * path.
 */
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void shouldRegisterUser_whenUsernameAndEmailAreFree() {
        RegistrationCommand command = new RegistrationCommand("reagan", "reaganfwambaa@gmail.com", "S3cret!", 25);

        when(userRepository.existsByUsername("reagan")).thenReturn(false);
        when(userRepository.existsByEmail("reaganfwambaa@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegisteredUser result = registrationService.register(command);

        assertThat(result.username()).isEqualTo("reagan");
        assertThat(result.email()).isEqualTo("reaganfwambaa@gmail.com");
    }

    @Test
    void shouldThrow_whenUsernameAlreadyExists() {
        RegistrationCommand command = new RegistrationCommand("reagan", "new@example.com", "S3cret!", 25);

        when(userRepository.existsByUsername("reagan")).thenReturn(true);

        assertThatThrownBy(() -> registrationService.register(command))
                .isInstanceOf(UsernameAlreadyExistsException.class);

        // Confirms the early-return pattern: if username exists, we never
        // even check email or attempt a save.
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrow_whenEmailAlreadyExists() {
        RegistrationCommand command = new RegistrationCommand("newuser", "taken@example.com", "S3cret!", 25);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> registrationService.register(command))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }
}
