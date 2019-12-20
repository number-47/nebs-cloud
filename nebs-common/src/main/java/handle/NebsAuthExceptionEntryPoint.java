package handle;

import com.alibaba.fastjson.JSONObject;
import entity.NebsResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import util.NebsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author number47
 * @date 2019/11/22 03:02
 * @description 用于处理401类型异常
 */
public class NebsAuthExceptionEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException {
		NebsResponse nebsResponse = new NebsResponse();
		NebsUtil.makeResponse(
						response, MediaType.APPLICATION_JSON_UTF8_VALUE,
						HttpServletResponse.SC_UNAUTHORIZED, nebsResponse.message("token无效")
		);
	}
}
