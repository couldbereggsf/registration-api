package com.reggs.registration.persistence.repository;

import com.reggs.registration.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindByUsername_whenUserExists() {
        User user = new User("reagan", "reagan@example.com", "hashed-password", 25);
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
        User user = new User("lucy", "lucy@example.com", "hashed-password", 22);
        userRepository.save(user);

        assertThat(userRepository.existsByEmail("lucy@example.com")).isTrue();
    }
}
