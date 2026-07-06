package com.reggs.registration.service;

import com.reggs.registration.common.exception.EmailAlreadyExistsException;
import com.reggs.registration.common.exception.UsernameAlreadyExistsException;
import com.reggs.registration.persistence.entity.User;
import com.reggs.registration.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * This is where my Default-group, DB-hitting validation step from
 * Project 1 actually lives: existsByUsername / existsByEmail. It runs
 * AFTER Jakarta Validation's FirstCheck and SecondCheck groups have
 * already passed in the web layer's @GroupSequence - by the time a
 * request reaches this service, we know the shape of the data is sound,
 * and we only need to ask "does this already exist?"
 *
 * Early-return pattern throughout, per my JBE notes: "if the user
 * doesn't exist, we return empty / fail fast" rather than nested
 * branching.
 */
@Service
public class RegistrationService {

    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisteredUser register(RegistrationCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new UsernameAlreadyExistsException(command.username());
        }

        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyExistsException(command.email());
        }

        User user = new User(
                command.username(),
                command.email(),
                hashPassword(command.password()),
                command.age()
        );

        User saved = userRepository.save(user);

        return new RegisteredUser(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getCreatedAt());
    }

    /**
     * Placeholder only which is exactly the same caveat flagged in the JBE
     * notes about plain-text checks being temporary. I will swap this for
     * Spring Security's PasswordEncoder (BCrypt) as the real next step.
     */
    private String hashPassword(String rawPassword) {
        return "PLAINTEXT_PLACEHOLDER:" + rawPassword;
    }
}
