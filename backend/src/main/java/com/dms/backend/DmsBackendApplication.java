package com.dms.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmsBackendApplication.class, args);
	}

}
