package com.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition
@SpringBootApplication()
@EnableJpaRepositories()
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
