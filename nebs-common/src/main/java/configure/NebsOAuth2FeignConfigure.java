package configure;

import entity.constant.NebsConstant;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.Base64Utils;

/**
 * @author number47
 * @date 2019/11/24 14:00
 * @description
 */
public class NebsOAuth2FeignConfigure {

	/**
	 * 通过SecurityContextHolder从请求上下文中获取了OAuth2AuthenticationDetails类型对象,取到了请求令牌，
	 * 然后在请求模板对象requestTemplate的头部手动将令牌添加上去
	 * 拦截Feign请求，手动往请求头上加入令牌即可
	 * @return
	 */
	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor() {
		return requestTemplate -> {
			// 添加  TokenHeader
			String tokenHeader = new String(Base64Utils.encode(NebsConstant.GATEWAY_TOKEN_HEADER.getBytes()));
			requestTemplate.header(NebsConstant.GATEWAY_TOKEN_VALUE, tokenHeader);

			Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
			if (details instanceof OAuth2AuthenticationDetails) {
				String authorizationToken = ((OAuth2AuthenticationDetails) details).getTokenValue();
				requestTemplate.header(HttpHeaders.AUTHORIZATION, "bearer " + authorizationToken);
			}
		};
	}
}
