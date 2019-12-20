package com.number47.nebs.auth.service;


import com.number47.nebs.auth.manage.UserManager;
import entity.NebsAuthUser;
import entity.system.SystemUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * @author number47
 * @date 2019/11/20 01:54
 * @description 用于校验用户名密码
 */
@Service
public class NebsUserDetailService implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserManager userManager;

	/*@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SystemUser systemUser = userManager.findByName(username);
		if (systemUser != null) {
			String permissions = userManager.findUserPermissions(systemUser.getUsername());
			boolean notLocked = false;
			if (StringUtils.equals(SystemUser.STATUS_VALID, systemUser.getStatus())){
				notLocked = true;
			}
			NebsAuthUser authUser = new NebsAuthUser(systemUser.getUsername(), systemUser.getPassword(),
													true, true, true, notLocked,
													AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
			return transSystemUserToAuthUser(authUser,systemUser);
		} else {
			throw new UsernameNotFoundException("");
		}
	}

	private NebsAuthUser transSystemUserToAuthUser(NebsAuthUser authUser, SystemUser systemUser) {
		authUser.setAvatar(systemUser.getAvatar());
		authUser.setDeptId(systemUser.getDeptId());
		authUser.setDeptName(systemUser.getDeptName());
		authUser.setEmail(systemUser.getEmail());
		authUser.setMobile(systemUser.getMobile());
		authUser.setRoleId(systemUser.getRoleId());
		authUser.setRoleName(systemUser.getRoleName());
		authUser.setSex(systemUser.getSex());
		authUser.setUserId(systemUser.getUserId());
		authUser.setLastLoginTime(systemUser.getLastLoginTime());
		authUser.setDescription(systemUser.getDescription());
		authUser.setStatus(systemUser.getStatus());
		return authUser;
	}*/
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SystemUser systemUser = userManager.findByName(username);
		if (systemUser != null) {
			String permissions = userManager.findUserPermissions(systemUser.getUsername());
			boolean notLocked = false;
			if (StringUtils.equals(SystemUser.STATUS_VALID, systemUser.getStatus()))
				notLocked = true;
			NebsAuthUser authUser = new NebsAuthUser(systemUser.getUsername(), systemUser.getPassword(), true, true, true, notLocked,
					AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

			BeanUtils.copyProperties(systemUser,authUser);
			return authUser;
		} else {
			throw new UsernameNotFoundException("");
		}
	}
}
