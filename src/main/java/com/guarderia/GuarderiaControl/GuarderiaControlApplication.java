package com.guarderia.GuarderiaControl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GuarderiaControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuarderiaControlApplication.class, args);
	}

}
