package com.reggs.registration.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = "com.reggs.registration")
public class RegistrationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationApiApplication.class, args);
    }
}
