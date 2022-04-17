package com.cactus.desert.desertbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.cactus.desert.desertbackend.entity")
@ComponentScan(basePackages = "com.cactus.desert.desertbackend.service")
@ComponentScan(basePackages = "com.cactus.desert.desertbackend.controller")
@EnableJpaRepositories(basePackages = "com.cactus.desert.desertbackend.repository")
public class DesertBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesertBackendApplication.class, args);
    }

}
