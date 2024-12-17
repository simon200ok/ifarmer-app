package com.ifarmr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IFarmrApplication {

	public static void main(String[] args) {
		SpringApplication.run(IFarmrApplication.class, args);
	}

}
