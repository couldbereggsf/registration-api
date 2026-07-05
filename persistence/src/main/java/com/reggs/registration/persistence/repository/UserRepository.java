package com.reggs.registration.persistence.repository;

import com.reggs.registration.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Relies on Spring Boot's automatic query generation (the "find by
 * username" pattern from JBE Episode 1) rather than hand-written JPQL.
 * existsByUsername / existsByEmail back the Default-group uniqueness
 * check in the service layer - this is the one validation step that
 * actually has to hit the database, which is exactly why it runs last.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
