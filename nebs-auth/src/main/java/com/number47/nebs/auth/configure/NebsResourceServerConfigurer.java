package com.number47.nebs.auth.configure;

import com.number47.nebs.auth.properties.NebsAuthProperties;
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
 * @date 2019/11/20 01:48
 * @description 资源服务器的配置类
 */
@Configuration
@EnableResourceServer
public class NebsResourceServerConfigurer extends ResourceServerConfigurerAdapter {

	@Autowired
	private NebsAccessDeniedHandler accessDeniedHandler;
	@Autowired
	private NebsAuthExceptionEntryPoint exceptionEntryPoint;
	@Autowired
	private NebsAuthProperties properties;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

		http.csrf().disable()
				.requestMatchers().antMatchers("/**")
				.and()
				.authorizeRequests()
				.antMatchers(anonUrls).permitAll()
				.antMatchers("/**").authenticated()
				.and().httpBasic();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.authenticationEntryPoint(exceptionEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);
	}
}