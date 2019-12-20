package com.number47.nebs.auth.entity;

import lombok.Data;

/**
 * @author number47
 * @date 2019/11/22 02:46
 * @description
 */
@Data
public class NebsClientsProperties {

	private String client;
	private String secret;
	private String grantType = "password,authorization_code,refresh_token";
	private String scope = "all";
}
