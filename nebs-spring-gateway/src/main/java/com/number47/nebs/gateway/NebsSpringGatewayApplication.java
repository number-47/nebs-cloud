package com.number47.nebs.gateway;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author liupeitao
 * @date 2021/9/18 17:45
 * @description
 */
@SpringBootApplication
public class NebsSpringGatewayApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NebsSpringGatewayApplication.class)
				.web(WebApplicationType.REACTIVE)
				.run(args);
	}

}
