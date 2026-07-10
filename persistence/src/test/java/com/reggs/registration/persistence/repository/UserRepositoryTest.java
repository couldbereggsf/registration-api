package com.reggs.registration.persistence.repository;

import com.reggs.registration.persistence.PersistenceTestConfig;
import com.reggs.registration.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Test approach: Uses the repository to save a user,
 * then asserts that the repository can successfully find it.
 *
 * To follow "don't trust a test you haven't seen fail" - try temporarily
 * changing existsByUsername("reagan") to existsByUsername("someone-else")
 * below and re-run; shouldFindByUsername_whenUserExists should fail.
 * Revert it once you've confirmed that.
 */
@DataJpaTest
@ContextConfiguration(classes = PersistenceTestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindByUsername_whenUserExists() {
        // GIVEN
        User user = new User("reagan", "reaganfwambaa@gmail.com", "hashed-password", 25);
        userRepository.save(user);

        // WHEN
        boolean exists = userRepository.existsByUsername("reagan");

        // THEN
        assertThat(exists).isTrue();
    }
}