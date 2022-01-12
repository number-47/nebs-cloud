package com.number47.nebs.gateway.common.controller;

import entity.NebsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author number47
 * @date 2021/9/30 15:47
 * @description
 */
@RestController
public class FallbackController {

	@RequestMapping("fallback/{name}")
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<NebsResponse> systemFallback(@PathVariable String name) {
		String response = String.format("访问%s超时或者服务不可用", name);
		return Mono.just(new NebsResponse().message(response));
	}
}
