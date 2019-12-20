package com.number47.nebs.server.test.controller;

import com.number47.nebs.server.test.service.IHelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author number47
 * @date 2019/11/21 01:55
 * @description
 */
@Slf4j
@RestController
public class TestController {
	@Autowired
	private IHelloService helloService;

	@GetMapping("hello")
	public String hello(String name){
		log.info("Feign调用nebs-server-system的/hello服务");
		return this.helloService.hello(name);
	}

	@GetMapping("test1")
	public String test1(){
		return "拥有'user:add'权限";
	}

	@GetMapping("test2")
	public String test2(){
		return "拥有'user:update'权限";
	}

}
