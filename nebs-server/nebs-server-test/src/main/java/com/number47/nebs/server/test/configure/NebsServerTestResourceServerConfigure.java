package com.number47.nebs.server.test.configure;

import handle.NebsAccessDeniedHandler;
import handle.NebsAuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author number47
 * @date 2019/11/21 01:54
 * @description
 */
@Configuration
@EnableResourceServer
public class NebsServerTestResourceServerConfigure extends ResourceServerConfigurerAdapter {

	@Autowired
	private NebsAccessDeniedHandler accessDeniedHandler;
	@Autowired
	private NebsAuthExceptionEntryPoint exceptionEntryPoint;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.requestMatchers().antMatchers("/**")
				.and()
				.authorizeRequests().antMatchers("/actuator/**").permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/**").authenticated();
	}



	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.authenticationEntryPoint(exceptionEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);
	}
}