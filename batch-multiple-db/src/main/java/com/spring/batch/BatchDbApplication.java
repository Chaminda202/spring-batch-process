package com.spring.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BatchDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchDbApplication.class, args);
	}

}
