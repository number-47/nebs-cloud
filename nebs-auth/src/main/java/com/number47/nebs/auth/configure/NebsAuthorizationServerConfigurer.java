package com.number47.nebs.auth.configure;

import com.number47.nebs.auth.properties.NebsAuthProperties;
import com.number47.nebs.auth.entity.NebsClientsProperties;
import com.number47.nebs.auth.service.NebsUserDetailService;
import com.number47.nebs.auth.translator.NebsWebResponseExceptionTranslator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author number47
 * @date 2019/11/20 01:52
 * @description 认证服务器相关的安全配置类
 */
@Configuration
@EnableAuthorizationServer
public class NebsAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Autowired
	private NebsUserDetailService userDetailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private NebsWebResponseExceptionTranslator exceptionTranslator;
	@Autowired
	private NebsAuthProperties authProperties;


	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		NebsClientsProperties[] clientsArray = authProperties.getClients();
		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
		if (ArrayUtils.isNotEmpty(clientsArray)) {
			for (NebsClientsProperties client : clientsArray) {
				if (StringUtils.isBlank(client.getClient())) {
					throw new Exception("client不能为空");
				}
				if (StringUtils.isBlank(client.getSecret())) {
					throw new Exception("secret不能为空");
				}
				String[] grantTypes = StringUtils.splitByWholeSeparatorPreserveAllTokens(client.getGrantType(), ",");
				builder.withClient(client.getClient())
						.secret(passwordEncoder.encode(client.getSecret()))
						.authorizedGrantTypes(grantTypes)
						.scopes(client.getScope());
			}
		}
	}

	@Override
	@SuppressWarnings("all")
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.tokenStore(tokenStore())
				.userDetailsService(userDetailService)
				.authenticationManager(authenticationManager)
				.tokenServices(defaultTokenServices())
				.exceptionTranslator(exceptionTranslator);;
	}

	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}

	@Primary
	@Bean
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setAccessTokenValiditySeconds(authProperties.getAccessTokenValiditySeconds());
		tokenServices.setRefreshTokenValiditySeconds(authProperties.getRefreshTokenValiditySeconds());
		return tokenServices;
	}
}