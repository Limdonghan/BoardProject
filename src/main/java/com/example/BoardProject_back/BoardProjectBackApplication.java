package com.example.BoardProject_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoardProjectBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBackApplication.class, args);
	}

}
