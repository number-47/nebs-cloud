package com.number47.nebs.server.system.configure;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.number47.nebs.server.system.properties.NebsServerSystemProperties;
import com.number47.nebs.server.system.properties.NebsSwaggerProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author number47
 * @date 2019/12/1 23:34
 * @description MyBatis Plus分页插件配置
 */
@Configuration
@EnableSwagger2
public class NebsWebConfigure {

	@Autowired
	private NebsServerSystemProperties properties;

	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		List<ISqlParser> sqlParserList = new ArrayList<>();
		sqlParserList.add(new BlockAttackSqlParser());
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}

	/**
	 * swagger相关配置
	 */
	@Bean
	public Docket swaggerApi() {
		NebsSwaggerProperties swagger = properties.getSwagger();
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo(swagger))
				.securitySchemes(Collections.singletonList(securityScheme(swagger)))
				.securityContexts(Collections.singletonList(securityContext(swagger)));
	}

	private ApiInfo apiInfo(NebsSwaggerProperties swagger) {
		return new ApiInfo(
				swagger.getTitle(),
				swagger.getDescription(),
				swagger.getVersion(),
				null,
				new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()),
				swagger.getLicense(), swagger.getLicenseUrl(), Collections.emptyList());

	}

	/**
	 * 通过OAuthBuilder对象构建了安全策略，主要配置了认证类型为ResourceOwnerPasswordCredentialsGrant（即密码模式），
	 * 认证地址为http://localhost:8301/auth/oauth/token（即通过网关转发到认证服务器），scope为test
	 * @param swagger
	 * @return
	 */
	private SecurityScheme securityScheme(NebsSwaggerProperties swagger) {
		GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(swagger.getGrantUrl());

		return new OAuthBuilder()
				.name(swagger.getName())
				.grantTypes(Collections.singletonList(grantType))
				.scopes(Arrays.asList(scopes(swagger)))
				.build();
	}

	private SecurityContext securityContext(NebsSwaggerProperties swagger) {
		return SecurityContext.builder()
				.securityReferences(Collections.singletonList(new SecurityReference(swagger.getName(), scopes(swagger))))
				.forPaths(PathSelectors.any())
				.build();
	}

	private AuthorizationScope[] scopes(NebsSwaggerProperties swagger) {
		return new AuthorizationScope[]{
				new AuthorizationScope(swagger.getScope(), StringUtils.EMPTY)
		};
	}
}
