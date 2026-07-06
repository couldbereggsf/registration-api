package com.reggs.registration.persistence.repository;

import com.reggs.registration.persistence.PersistenceTestConfig;
import com.reggs.registration.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

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