package com.number47.nebs.auth;

import annotation.EnableNebsAuthExceptionHandler;
import annotation.EnableNebsLettuceRedis;
import annotation.EnableNebsServerProtect;
import annotation.NebsCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@NebsCloudApplication
//@EnableNebsOauth2FeignClient
//@EnableNebsAuthExceptionHandler
//@EnableNebsServerProtect
@EnableNebsLettuceRedis
@MapperScan("com.number47.nebs.auth.mapper")
public class NebsAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(NebsAuthApplication.class, args);
	}

}
