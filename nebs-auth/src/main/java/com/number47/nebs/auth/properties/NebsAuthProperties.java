package com.number47.nebs.auth.properties;

import com.number47.nebs.auth.entity.NebsClientsProperties;
import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author number47
 * @date 2019/11/22 02:47
 * @description
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:nebs-auth.properties"})
@ConfigurationProperties(prefix = "nebs.auth")
public class NebsAuthProperties {

	private NebsClientsProperties[] clients = {};
	/**
	 * access_token的有效时间
	 */
	private int accessTokenValiditySeconds = 60 * 60 * 24;
	/**
	 * refresh_token的有效时间
	 */
	private int refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

	//验证码配置类
	private NebsValidateCodeProperties code = new NebsValidateCodeProperties();
	// 免认证路径
	private String anonUrl;

	// 令牌存储方式
	private String tokenStoreType;
}
