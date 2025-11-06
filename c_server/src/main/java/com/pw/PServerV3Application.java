package com.pw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.pw"})
@EnableScheduling
public class PServerV3Application {
	public static void main(String[] args) {
		SpringApplication.run(PServerV3Application.class, args);
	}

}
