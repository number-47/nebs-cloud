package com.number47.nebs.gateway.fallback;

import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.BlockResponse;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.ZuulBlockFallbackProvider;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.http.HttpStatus;

/**
 * @author number47
 * @date 2019/12/1 23:25
 * @description 自定义异常响应
 */
public class NebsGatewayBlockFallbackProvider implements ZuulBlockFallbackProvider {
	@Override
	public String getRoute() {
		return "*";
	}

	@Override
	public BlockResponse fallbackResponse(String route, Throwable throwable) {
		if (throwable instanceof BlockException) {
			return new BlockResponse(HttpStatus.TOO_MANY_REQUESTS.value(),
					"访问频率超限", route);
		} else {
			return new BlockResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"系统异常", route);
		}
	}
}