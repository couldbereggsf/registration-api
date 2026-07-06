package com.reggs.registration.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * The persisted User record - tried the same pattern as the JBE Episode 1 entity
 * layer (identity primary key, username/password/email columns), now
 * isolated in its own module.
 *
 * Note: password is stored as a hash placeholder in the service layer
 * for this project. Real password hashing (BCrypt via Spring Security)
 * is the next real step, exactly as flagged in the JBE notes.
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Instant createdAt;

    protected User() {
        // required by JPA
    }

    public User(String username, String email, String passwordHash, Integer age) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.age = age;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Integer getAge() {
        return age;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setUsername(String reagan) {
    }
}
