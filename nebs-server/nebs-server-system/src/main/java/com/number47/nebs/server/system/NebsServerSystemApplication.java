package com.number47.nebs.server.system;

import annotation.EnableNebsAuthExceptionHandler;
import annotation.EnableNebsOauth2FeignClient;
import annotation.EnableNebsServerProtect;
import annotation.NebsCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableDiscoveryClient nacos不需要这个
@SpringBootApplication
@NebsCloudApplication
//@EnableNebsOauth2FeignClient
//@EnableNebsAuthExceptionHandler
//@EnableNebsServerProtect
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement //@Transactional注解用于开启数据库事务
@MapperScan("com.number47.nebs.server.system.mapper")
public class NebsServerSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NebsServerSystemApplication.class, args);
	}

}
