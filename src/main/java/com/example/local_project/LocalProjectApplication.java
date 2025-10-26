package com.example.local_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LocalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalProjectApplication.class, args);
		System.out.println("Server is running on port 8080");
	}
	
}