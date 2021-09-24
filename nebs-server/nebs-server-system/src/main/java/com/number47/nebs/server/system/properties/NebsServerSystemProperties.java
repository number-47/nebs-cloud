package com.number47.nebs.server.system.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author number47
 * @date 2019/12/2 01:40
 * @description
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:nebs-server-system.properties"})
@ConfigurationProperties(prefix = "nebs.server.system")
public class NebsServerSystemProperties {
	/**
	 * 免认证 URI，多个值的话以逗号分隔
	 */
	private String anonUrl;

	private NebsSwaggerProperties swagger = new NebsSwaggerProperties();

	private Integer batchInsertMaxNum;
}
