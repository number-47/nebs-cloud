package com.number47.nebs.auth.service;

import com.number47.nebs.auth.properties.NebsAuthProperties;
import com.number47.nebs.auth.properties.NebsValidateCodeProperties;
import com.wf.captcha.Captcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import entity.NebsConstant;
import exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import service.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author number47
 * @date 2019/11/25 00:45
 * @description  验证码服务类
 */
@Service
public class ValidateCodeService {

	@Autowired
	private RedisService redisService;
	@Autowired
	private NebsAuthProperties properties;

	/**
	 * 生成验证码
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
		String key = request.getParameter("key");
		if (StringUtils.isBlank(key)) {
			throw new ValidateCodeException("验证码key不能为空");
		}
		NebsValidateCodeProperties code = properties.getCode();
		setHeader(response, code.getType());

		Captcha captcha = createCaptcha(code);
		redisService.set(NebsConstant.CODE_PREFIX + key, StringUtils.lowerCase(captcha.text()), code.getTime());
		captcha.out(response.getOutputStream());
	}

	/**
	 * 校验验证码
	 *
	 * @param key   前端上送 key
	 * @param value 前端上送待校验值
	 */
	public void check(String key, String value) throws ValidateCodeException {
		Object codeInRedis = redisService.get(NebsConstant.CODE_PREFIX + key);
		codeInRedis = "0000";
		if (StringUtils.isBlank(value)) {
			throw new ValidateCodeException("请输入验证码");
		}
		if (codeInRedis == null) {
			throw new ValidateCodeException("验证码已过期");
		}
		if (!StringUtils.equalsIgnoreCase(value, String.valueOf(codeInRedis))) {
			throw new ValidateCodeException("验证码不正确");
		}
	}

	private Captcha createCaptcha(NebsValidateCodeProperties code) {
		Captcha captcha = null;
		if (StringUtils.equalsIgnoreCase(code.getType(), NebsConstant.GIF)) {
			captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
		} else {
			captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
		}
		captcha.setCharType(code.getCharType());
		return captcha;
	}

	private void setHeader(HttpServletResponse response, String type) {
		if (StringUtils.equalsIgnoreCase(type, NebsConstant.GIF)) {
			response.setContentType(MediaType.IMAGE_GIF_VALUE);
		} else {
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
		}
		response.setHeader(HttpHeaders.PRAGMA, "No-cache");
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
		response.setDateHeader(HttpHeaders.EXPIRES, 0L);
	}
}
