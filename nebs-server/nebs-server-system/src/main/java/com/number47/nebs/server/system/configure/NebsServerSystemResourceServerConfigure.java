package com.number47.nebs.server.system.configure;

import com.number47.nebs.server.system.properties.NebsServerSystemProperties;
import handle.NebsAccessDeniedHandler;
import handle.NebsAuthExceptionEntryPoint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author number47
 * @date 2019/11/21 01:43
 * @description 资源服务器配置类
 */
@Configuration
@EnableResourceServer
public class NebsServerSystemResourceServerConfigure extends ResourceServerConfigurerAdapter {

	@Autowired
	private NebsAccessDeniedHandler accessDeniedHandler;
	@Autowired
	private NebsAuthExceptionEntryPoint exceptionEntryPoint;
	@Autowired
	private NebsServerSystemProperties properties;


	/**
	 * 所有访问nebs-server-system的请求都需要认证，只有通过认证服务器发放的令牌才能进行访问
	 * @param http
	 * @throws Exception
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");
		http.csrf().disable()
				.requestMatchers().antMatchers("/**")
				.and()
				.authorizeRequests()
				.antMatchers(anonUrls).permitAll()
				.antMatchers("/**").authenticated();
	}


	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.authenticationEntryPoint(exceptionEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);
	}
}