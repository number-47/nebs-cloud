package interceptor;

import com.alibaba.fastjson.JSONObject;
import entity.NebsResponse;
import entity.constant.NebsConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author number47
 * @date 2019/11/24 14:26
 * @description 校验Zuul Token
 */
public class NebsServerProtectInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		// 从请求头中获取 Zuul Token
		String token = request.getHeader(NebsConstant.GATEWAY_TOKEN_VALUE);
		String zuulToken = new String(Base64Utils.encode(NebsConstant.GATEWAY_TOKEN_HEADER.getBytes()));
		// 校验 Zuul Token的正确性
		if (StringUtils.equals(zuulToken, token)) {
			return true;
		} else {
			NebsResponse nebsResponse = new NebsResponse();
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write(JSONObject.toJSONString(nebsResponse.message("请通过网关获取资源")));
			return false;
		}
	}
}
