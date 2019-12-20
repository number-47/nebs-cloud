package com.number47.nebs.server.system.server;

import com.baomidou.mybatisplus.extension.service.IService;
import entity.system.UserRole;

public interface IUserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String[] roleIds);

	void deleteUserRolesByUserId(String[] userIds);
}
