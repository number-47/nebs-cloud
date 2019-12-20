package com.number47.nebs.gateway;

import annotation.EnableNebsServerProtect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy       //开启Zuul服务网关功能
@EnableDiscoveryClient //开启服务注册与发现
@EnableNebsServerProtect
@SpringBootApplication
public class NebsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NebsGatewayApplication.class, args);
	}

}
