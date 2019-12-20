package com.number47.nebs.server.system.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.number47.nebs.server.system.mapper.UserRoleMapper;
import com.number47.nebs.server.system.server.IUserRoleService;
import entity.system.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author number47
 * @date 2019/12/1 23:48
 * @description
 */
@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

	@Override
	@Transactional
	public void deleteUserRolesByRoleId(String[] roleIds) {
		Arrays.stream(roleIds).forEach(id -> baseMapper.deleteByRoleId(Long.valueOf(id)));
	}

	@Override
	@Transactional
	public void deleteUserRolesByUserId(String[] userIds) {
		Arrays.stream(userIds).forEach(id -> baseMapper.deleteByUserId(Long.valueOf(id)));
	}
}