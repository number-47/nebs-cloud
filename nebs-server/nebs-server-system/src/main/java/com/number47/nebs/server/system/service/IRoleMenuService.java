package com.number47.nebs.server.system.service;

import entity.system.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IRoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenusByRoleId(String[] roleIds);

    void deleteRoleMenusByMenuId(String[] menuIds);

    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}
