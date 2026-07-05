package com.reggs.registration.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.reggs.registration")
@EntityScan("com.reggs.registration.persistence.entity")
@EnableJpaRepositories("com.reggs.registration.persistence.repository")
public class RegistrationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationApiApplication.class, args);
    }
}
