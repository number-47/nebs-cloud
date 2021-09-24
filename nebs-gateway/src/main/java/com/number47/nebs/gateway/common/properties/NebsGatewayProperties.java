package com.number47.nebs.gateway.common.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author number47
 * @date 2019/12/3 23:44
 * @description
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:nebs-gateway.properties"})
@ConfigurationProperties(prefix = "nebs.gateway")
public class NebsGatewayProperties {
	/**
	 * 禁止外部访问的 URI，多个值的话以逗号分隔
	 */
	private String forbidRequestUri;
}
