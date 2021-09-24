package com.number47.nebs.gateway.common.filter;

import com.netflix.zuul.context.RequestContext;
import entity.NebsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import util.NebsUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * @author number47
 * @date 2019/11/22 03:25
 * @description 自定义Zuul异常处理可以通过继承Zuul的SendErrorFilter过滤器来实现。
 */
@Slf4j
@Component
public class NebsGatewayErrorFilter extends SendErrorFilter {

	@Override
	public Object run() {
		try {
			NebsResponse nebsResponse = new NebsResponse();
			RequestContext ctx = RequestContext.getCurrentContext();
			String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);

			ExceptionHolder exception = findZuulException(ctx.getThrowable());
			String errorCause = exception.getErrorCause();
			Throwable throwable = exception.getThrowable();
			String message = throwable.getMessage();
			message = StringUtils.isBlank(message) ? errorCause : message;
			nebsResponse = resolveExceptionMessage(message, serviceId, nebsResponse);

			HttpServletResponse response = ctx.getResponse();
			NebsUtil.makeResponse(
					response, MediaType.APPLICATION_JSON_UTF8_VALUE,
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR, nebsResponse
			);
			log.error("Zull sendError：{}", nebsResponse.getMessage());
		} catch (Exception ex) {
			log.error("Zuul sendError", ex);
			ReflectionUtils.rethrowRuntimeException(ex);
		}
		return null;
	}

	private NebsResponse resolveExceptionMessage(String message, String serviceId, NebsResponse nebsResponse) {
		if (StringUtils.containsIgnoreCase(message, "time out")) {
			return nebsResponse.message("请求" + serviceId + "服务超时");
		}
		if (StringUtils.containsIgnoreCase(message, "forwarding error")) {
			return nebsResponse.message(serviceId + "服务不可用");
		}
		return nebsResponse.message("Zuul请求" + serviceId + "服务异常");
	}

}
