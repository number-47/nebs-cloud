package com.number47.nebs.gateway.common.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author number47
 * @date 2021/9/30 15:50
 * @description  跨域配置
 */
@Configuration
public class NebsGateWayCorsConfigure {

	@Bean
	public CorsWebFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
		CorsConfiguration cors = new CorsConfiguration();
		cors.setAllowCredentials(true);
		cors.addAllowedOrigin(CorsConfiguration.ALL);
		cors.addAllowedHeader(CorsConfiguration.ALL);
		cors.addAllowedMethod(CorsConfiguration.ALL);
		source.registerCorsConfiguration("/**", cors);
		return new CorsWebFilter(source);
	}
}
