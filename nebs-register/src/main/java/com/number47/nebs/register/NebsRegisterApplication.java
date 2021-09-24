package com.number47.nebs.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class NebsRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(NebsRegisterApplication.class, args);
	}

}
