package com.number47.nebs.auth.configure;

import com.number47.nebs.auth.filter.ValidateCodeFilter;
import com.number47.nebs.auth.service.NebsUserDetailService;
import entity.constant.EndpointConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author number47
 * @date 2019/11/20 01:45
 * @description 安全配置类  /oauth/开头的请求由NebsSecurityConfigure过滤器链处理，
 * 剩下的其他请求由NebsResourceServerConfigurer过滤器链处理。
 */
@Order(2)
@EnableWebSecurity
public class NebsSecurityConfigure extends WebSecurityConfigurerAdapter {

	@Autowired
	private NebsUserDetailService userDetailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ValidateCodeFilter validateCodeFilter;

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
				.requestMatchers()
				.antMatchers(EndpointConstant.OAUTH_ALL) //安全配置类只对/oauth/开头的请求有效
				.and()
				.authorizeRequests()
				.antMatchers(EndpointConstant.OAUTH_ALL).authenticated()
				.and()
				.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
	}
}
