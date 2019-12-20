package com.number47.nebs.auth.controller;

import com.number47.nebs.auth.exception.NebsAuthException;
import com.number47.nebs.auth.service.ValidateCodeService;
import entity.NebsResponse;
import exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * @author number47
 * @date 2019/11/20 01:58
 * @description 对外提供一些REST服务
 */
@RestController
public class SecurityController {

	@Autowired
	private ConsumerTokenServices consumerTokenServices;

	@Autowired
	private ValidateCodeService validateCodeService;

	@GetMapping("captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
		validateCodeService.create(request, response);
	}

	@GetMapping("oauth/test")
	public String testOauth() {
		return "oauth";
	}

	@GetMapping("user")
	public Principal currentUser(Principal principal) {
		return principal;
	}

	@DeleteMapping("signout")
	public NebsResponse signout(HttpServletRequest request) throws NebsAuthException {
		String authorization = request.getHeader("Authorization");
		String token = StringUtils.replace(authorization, "bearer ", "");
		NebsResponse nebsResponse = new NebsResponse();
		if (!consumerTokenServices.revokeToken(token)) {
			throw new NebsAuthException("退出登录失败");
		}
		return nebsResponse.message("退出登录成功");
	}
}
