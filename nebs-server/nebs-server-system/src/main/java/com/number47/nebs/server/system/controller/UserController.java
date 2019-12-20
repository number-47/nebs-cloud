package com.number47.nebs.server.system.controller;

import annotation.ControllerEndpoint;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.number47.nebs.server.system.server.ILoginLogService;
import com.number47.nebs.server.system.server.IUserService;
import entity.NebsResponse;
import entity.QueryRequest;
import entity.system.LoginLog;
import exception.NebsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import entity.system.SystemUser;
import util.NebsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author number47
 * @date 2019/12/1 23:54
 * @description
 */
@Slf4j
@Validated
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ILoginLogService loginLogService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('user:view')")
	public NebsResponse userList(QueryRequest queryRequest, SystemUser user) {
		Map<String, Object> dataTable = NebsUtil.getDataTable(userService.findUserDetail(user, queryRequest));
		return new NebsResponse().data(dataTable);
	}

	@GetMapping("success/{username}")
	public void loginSuccess(@NotBlank(message = "{required}") @PathVariable String username, HttpServletRequest request) {
		// 更新登录时间
		this.userService.updateLoginTime(username);
		// 保存登录日志
		LoginLog loginLog = new LoginLog();
		loginLog.setUsername(username);
		loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
		this.loginLogService.saveLoginLog(loginLog);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('user:add')")
	@ControllerEndpoint(operation = "新增用户", exceptionMessage = "新增用户失败")
	public void addUser(@Valid SystemUser user) throws NebsException {
		try {
			this.userService.createUser(user);
		} catch (Exception e) {
			String message = "新增用户失败";
			log.error(message, e);
			throw new NebsException(message);
		}
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('user:update')")
	@ControllerEndpoint(operation = "修改用户", exceptionMessage = "修改用户失败")
	public void updateUser(@Valid SystemUser user) throws NebsException {
		try {
			this.userService.updateUser(user);
		} catch (Exception e) {
			String message = "修改用户失败";
			log.error(message, e);
			throw new NebsException(message);
		}
	}

	@DeleteMapping("/{userIds}")
	@PreAuthorize("hasAnyAuthority('user:delete')")
	@ControllerEndpoint(operation = "删除用户", exceptionMessage = "删除用户失败")
	public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws NebsException {
		try {
			String[] ids = userIds.split(StringPool.COMMA);
			this.userService.deleteUsers(ids);
		} catch (Exception e) {
			String message = "删除用户失败";
			log.error(message, e);
			throw new NebsException(message);
		}
	}
}
