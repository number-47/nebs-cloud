package com.number47.nebs.auth.filter;

import com.number47.nebs.auth.service.ValidateCodeService;
import entity.NebsResponse;
import exception.ValidateCodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import util.NebsUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author number47
 * @date 2019/12/1 23:11
 * @description 过滤器，用于拦截请求并校验验证码的正确性。
 */
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

	@Autowired
	private ValidateCodeService validateCodeService;

	/**
	 * 编写了验证码校验逻辑
	 * 当拦截的请求URI为/oauth/token，请求方法为POST并且请求参数grant_type为password的时候（对应密码模式获取令牌请求），
	 * 需要进行验证码校验。验证码校验调用的是之前定义的ValidateCodeService的check方法
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		String header = httpServletRequest.getHeader("Authorization");
		String clientId = getClientId(header, httpServletRequest);

		RequestMatcher matcher = new AntPathRequestMatcher("/oauth/token", HttpMethod.POST.toString());
		if (matcher.matches(httpServletRequest)
				&& StringUtils.equalsIgnoreCase(httpServletRequest.getParameter("grant_type"), "password")
				&& !StringUtils.equalsAnyIgnoreCase(clientId, "swagger")) {
			try {
				validateCode(httpServletRequest);
				filterChain.doFilter(httpServletRequest, httpServletResponse);
			} catch (ValidateCodeException e) {
				NebsResponse nebsResponse = new NebsResponse();
				NebsUtil.makeResponse(httpServletResponse, MediaType.APPLICATION_JSON_UTF8_VALUE,
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR, nebsResponse.message(e.getMessage()));
				log.error(e.getMessage(), e);
			}
		} else {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private void validateCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
		String code = httpServletRequest.getParameter("code");
		String key = httpServletRequest.getParameter("key");
		validateCodeService.check(key, code);
	}

	private String getClientId(String header, HttpServletRequest request) {
		String clientId = "";
		try {
			byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
			byte[] decoded;
			decoded = Base64.getDecoder().decode(base64Token);

			String token = new String(decoded, StandardCharsets.UTF_8);
			int delim = token.indexOf(":");
			if (delim != -1) {
				clientId = new String[]{token.substring(0, delim), token.substring(delim + 1)}[0];
			}
		} catch (Exception ignore) {
		}
		return clientId;
	}
}