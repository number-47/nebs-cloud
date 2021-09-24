package com.number47.nebs.server.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import entity.QueryRequest;
import entity.system.Role;

import java.util.List;

public interface IRoleService extends IService<Role> {

    IPage<Role> findRoles(Role role, QueryRequest request);

    List<Role> findUserRole(String userName);

    List<Role> findAllRoles();

    Role findByName(String roleName);

    void createRole(Role role);

    void deleteRoles(String[] roleIds);

    void updateRole(Role role);
}
