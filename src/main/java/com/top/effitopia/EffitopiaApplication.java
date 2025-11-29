package com.top.effitopia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class  EffitopiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EffitopiaApplication.class, args);
	}

}
