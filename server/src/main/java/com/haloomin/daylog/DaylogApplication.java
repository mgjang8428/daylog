package com.haloomin.daylog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DaylogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaylogApplication.class, args);
	}

}
