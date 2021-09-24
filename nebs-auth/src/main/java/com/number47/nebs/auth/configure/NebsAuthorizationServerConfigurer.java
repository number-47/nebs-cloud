package com.number47.nebs.auth.configure;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.number47.nebs.auth.entity.NebsClientsProperties;
import com.number47.nebs.auth.properties.NebsAuthProperties;
import com.number47.nebs.auth.service.NebsUserDetailService;
import com.number47.nebs.auth.translator.NebsWebResponseExceptionTranslator;
import entity.constant.NebsConstant;
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
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.UUID;

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
	private DynamicRoutingDataSource dynamicRoutingDataSource;
	@Resource
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
						.scopes(client.getScope())
						.accessTokenValiditySeconds(authProperties.getAccessTokenValiditySeconds())
						.refreshTokenValiditySeconds(authProperties.getRefreshTokenValiditySeconds());
			}
		}
	}

	@Override
	@SuppressWarnings("all")
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer = endpoints.tokenStore(tokenStore())
				.userDetailsService(userDetailService)
				.authenticationManager(authenticationManager)
				.exceptionTranslator(exceptionTranslator);
		if (NebsConstant.JWT_TOKEN_STORE.equals(authProperties.getTokenStoreType())) {
			authorizationServerEndpointsConfigurer.accessTokenConverter(jwtAccessTokenConverter());
		} else {
			authorizationServerEndpointsConfigurer.tokenServices(defaultTokenServices());
		}
	}

	@Bean
	public TokenStore tokenStore() {
		if (NebsConstant.IN_MEMORY_TOKEN_STORE.equals(authProperties.getTokenStoreType())){
			return new InMemoryTokenStore();
		} else if(NebsConstant.JDBC_TOKEN_STORE.equals(authProperties.getTokenStoreType())){
			DataSource febsCloudBase = dynamicRoutingDataSource.getDataSource("nebs_cloud_base");
			return new JdbcTokenStore(febsCloudBase);
		}else if(NebsConstant.JWT_TOKEN_STORE.equals(authProperties.getTokenStoreType())){
			return new JwtTokenStore(jwtAccessTokenConverter());
		} else if (NebsConstant.REDIS_TOKEN_STORE.equals(authProperties.getTokenStoreType())){
			RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
			// 解决每次生成的 token都一样的问题
			redisTokenStore.setAuthenticationKeyGenerator(oAuth2Authentication -> UUID.randomUUID().toString());
			return redisTokenStore;
		} else {
			return new InMemoryTokenStore();
		}
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) accessTokenConverter.getAccessTokenConverter();
		DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
		userAuthenticationConverter.setUserDetailsService(userDetailService);
		defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
		accessTokenConverter.setSigningKey("nebs");
		return accessTokenConverter;
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
