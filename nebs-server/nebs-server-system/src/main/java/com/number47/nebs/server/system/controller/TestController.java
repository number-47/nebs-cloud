package com.number47.nebs.server.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author number47
 * @date 2019/11/21 01:49
 * @description
 */
@Slf4j
@RestController
public class TestController {

	@GetMapping("info")
	public String test(){
		return "nebs-server-system";
	}

	@GetMapping("currentUser")
	public Principal currentUser(Principal principal) {
		log.info("/currentUser服务被调用");
		return principal;
	}

	@GetMapping("hello")
	public String hello(String name) {
		log.info("/hello服务被调用");
		return "hello " + name;
	}
}