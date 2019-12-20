package com.number47.nebs.auth.manage;

import com.number47.nebs.auth.mapper.MenuMapper;
import com.number47.nebs.auth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import entity.system.Menu;
import entity.system.SystemUser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author number47
 * @date 2019/11/24 21:07
 * @description
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserManager {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private MenuMapper menuMapper;

	/**
	 * 通过用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return 用户
	 */
	public SystemUser findByName(String username) {
		return userMapper.findByName(username);
	}


	/**
	 * 通过用户名查询用户权限串
	 *
	 * @param username 用户名
	 * @return 权限
	 */
	public String findUserPermissions(String username) {
		/*List<Menu> userPermissions = menuMapper.findUserPermissions(username);

		List<String> perms = new ArrayList<>();
		for (Menu m: userPermissions){
			perms.add(m.getPerms());
		}
		return StringUtils.join(perms, ",");*/

		List<Menu> userPermissions = menuMapper.findUserPermissions(username);
		return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(","));
	}
}
