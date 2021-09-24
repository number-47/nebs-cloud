package com.number47.nebs.gateway.common.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.number47.nebs.gateway.common.properties.NebsGatewayProperties;
import entity.NebsResponse;
import entity.constant.NebsConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import util.NebsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author number47
 * @date 2019/11/24 14:22
 * @description
 */
@Slf4j
@Component
public class NebsGatewayRequestFilter extends ZuulFilter {

	@Autowired
	private NebsGatewayProperties properties;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 6;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
		HttpServletRequest request = ctx.getRequest();
		String host = request.getRemoteHost();
		String method = request.getMethod();
		String uri = request.getRequestURI();
		log.info("请求URI：{}，HTTP Method：{}，请求IP：{}，ServerId：{}", uri, method, host, serviceId);

		// 禁止外部访问资源实现
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
			HttpServletResponse response = ctx.getResponse();
			NebsResponse nebsResponse = new NebsResponse().message("该URI不允许外部访问");
			try {

				NebsUtil.makeResponse(
						response, MediaType.APPLICATION_JSON_UTF8_VALUE,
						HttpServletResponse.SC_FORBIDDEN, nebsResponse
				);
				ctx.setSendZuulResponse(false);
				ctx.setResponse(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		byte[] token = Base64Utils.encode((NebsConstant.GATEWAY_TOKEN_HEADER).getBytes());
		ctx.addZuulRequestHeader(NebsConstant.GATEWAY_TOKEN_VALUE, new String(token));
		return null;
	}
}
