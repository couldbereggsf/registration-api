package com.reggs.registration.persistence.repository;

import com.reggs.registration.persistence.entity.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Mirrors the JBE Episode 1 repository test approach: use a real
 * TestEntityManager-backed in-memory DB, save a row, then assert the
 * repository can actually find it.
 *
 * To follow "don't trust a test you haven't seen fail" - try temporarily
 * changing existsByUsername("reagan") to existsByUsername("someone-else")
 * below and re-run; shouldFindByUsername_whenUserExists should fail.
 * Revert it once you've confirmed that.
 */
@DataJpaTest
class UserRepositoryTest {

    // This is an inner class to anchor the configuration
    @Nested
    @Configuration
    @Import(UserRepository.class) // Imports the repository explicitly
    @EnableJpaRepositories(basePackageClasses = UserRepository.class)
    @EntityScan(basePackageClasses = User.class)
    class TestConfig {
        // This acts as a mini-Spring context for the test
    }
    // Auto Wiring
        @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindByUsername_whenUserExists() {
        User user = new User("reagan", "reaganfwambaa@gmail.com", "hashed-password", 25);
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("reagan");

        assertThat(exists).isTrue();
    }

    @Test
    void shouldNotFindByUsername_whenUserDoesNotExist() {
        boolean exists = userRepository.existsByUsername("ghost");

        assertThat(exists).isFalse();
    }

    @Test
    void shouldFindByEmail_whenUserExists() {
        User user = new User("lucy", "lucynjenga@gmail.com", "hashed-password", 22);
        userRepository.save(user);

        assertThat(userRepository.existsByEmail("lucynjenga@gmail.com")).isTrue();
    }
}
