package com.number47.nebs.register.configure;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author number47
 * @date 2019/11/20 01:18
 * @description
 */
@EnableWebSecurity
public class NebsRegisterWebSecurityConfigure extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().ignoringAntMatchers("/eureka/**")
				.and()
				.authorizeRequests().antMatchers("/actuator/**").permitAll();
		super.configure(http);
	}
}