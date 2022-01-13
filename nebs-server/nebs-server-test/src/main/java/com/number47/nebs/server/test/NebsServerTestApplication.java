package com.number47.nebs.server.test;

import annotation.NebsCloudApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableFeignClients
//@EnableDiscoveryClient nacos不需要这个
@EnableGlobalMethodSecurity(prePostEnabled = true)
@NebsCloudApplication  //代替下面三个注解
//@EnableNebsAuthExceptionHandler //认证类型异常翻译
//@EnableNebsOauth2FeignClient  //开启带令牌的Feign请求，避免微服务内部调用出现401异常
//@EnableNebsServerProtect //开启微服务防护，避免客户端绕过网关直接请求微服务；
@SpringBootApplication
public class NebsServerTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NebsServerTestApplication.class, args);
	}

}
