package com.number47.nebs.gateway.common.filter;

import com.alibaba.fastjson.JSONObject;
import  com.number47.nebs.gateway.common.properties.NebsGatewayProperties;
import entity.NebsResponse;
import entity.constant.NebsConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * @author number47
 * @date 2021/9/18 17:56
 * @description
 */
@Slf4j
@Component
public class NebsGatewayRequestFilter implements  GlobalFilter {

	@Autowired
	private NebsGatewayProperties properties;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * ServerWebExchange对象获取到ServerHttpRequest请求和ServerHttpResponse响应对象
	 * @param exchange
	 * @param chain
	 * @return
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 自定义逻辑
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		// 禁止客户端的访问资源逻辑
		Mono<Void> checkForbidUriResult = checkForbidUri(request, response);
		if (checkForbidUriResult != null) {
			return checkForbidUriResult;
		}

		//日志打印
		printLog(exchange);

		byte[] token = Base64Utils.encode((NebsConstant.GATEWAY_TOKEN_HEADER).getBytes());
		ServerHttpRequest build = request.mutate().header(NebsConstant.GATEWAY_TOKEN_VALUE, new String(token)).build();
		ServerWebExchange newExchange = exchange.mutate().request(build).build();

		return chain.filter(newExchange);
	}

	private Mono<Void> checkForbidUri(ServerHttpRequest request, ServerHttpResponse response) {
		String uri = request.getPath().toString();
		boolean shouldForward = true;
		String forbidRequestUri = properties.getForbidRequestUri();
		String[] forbidRequestUris = StringUtils.splitByWholeSeparatorPreserveAllTokens(forbidRequestUri, ",");
		if (forbidRequestUris != null && ArrayUtils.isNotEmpty(forbidRequestUris)) {
			for (String u : forbidRequestUris) {
				if (pathMatcher.match(u, uri)) {
					shouldForward = false;
				}
			}
		}
		if (!shouldForward) {
			NebsResponse nebsResponse = new NebsResponse().message("该URI不允许外部访问");
			return makeResponse(response, nebsResponse);
		}
		return null;
	}

	private Mono<Void> makeResponse(ServerHttpResponse response, NebsResponse nebsResponse) {
		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(nebsResponse).getBytes());
		return response.writeWith(Mono.just(dataBuffer));
	}


	private void printLog(ServerWebExchange exchange) {
		URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
		LinkedHashSet<URI> uris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
		URI originUri = null;
		if (uris != null) {
			originUri = uris.stream().findFirst().orElse(null);
		}
		if (url != null && route != null && originUri != null) {
			log.info("转发请求：{}://{}{} --> 目标服务：{}，目标地址：{}://{}{}，转发时间：{}",
					originUri.getScheme(), originUri.getAuthority(), originUri.getPath(),
					route.getId(), url.getScheme(), url.getAuthority(), url.getPath(), LocalDateTime.now()
			);
		}
	}
}
