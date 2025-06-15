package com.sporty.jackpot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.sporty.jackpot",
		"com.sporty.jackpot.service.kafka",
		"com.sporty.jackpot.handler"
})
public class JackpotApplication {

	public static void main(String[] args) {
		SpringApplication.run(JackpotApplication.class, args);
	}

}
