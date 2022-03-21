package com.rs.depositoretiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DepositoretiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepositoretiroApplication.class, args);
	}

}
