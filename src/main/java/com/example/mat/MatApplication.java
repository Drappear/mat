package com.example.mat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatApplication.class, args);
	}

}
