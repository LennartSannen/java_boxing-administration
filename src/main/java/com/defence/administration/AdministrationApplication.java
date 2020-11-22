package com.defence.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SwaggerConfiguration.class)
@SpringBootApplication
public class AdministrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdministrationApplication.class, args);
	}

}
