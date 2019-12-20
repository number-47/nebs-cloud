package handle;

import entity.NebsResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import util.NebsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author number47
 * @date 2019/11/22 03:15
 * @description 用于处理403类型异常
 */
public class NebsAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		NebsResponse nebsResponse = new NebsResponse();
		NebsUtil.makeResponse(
				response, MediaType.APPLICATION_JSON_UTF8_VALUE,
				HttpServletResponse.SC_FORBIDDEN, nebsResponse.message("没有权限访问该资源"));
	}
}