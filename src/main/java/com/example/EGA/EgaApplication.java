package com.example.EGA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EgaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgaApplication.class, args);
	}

}
