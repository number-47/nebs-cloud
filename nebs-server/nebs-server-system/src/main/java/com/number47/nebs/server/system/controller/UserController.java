package com.number47.nebs.server.system.controller;

import annotation.ControllerEndpoint;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.number47.nebs.server.system.service.ILoginLogService;
import com.number47.nebs.server.system.service.IUserService;
import com.wuwenze.poi.ExcelKit;
import entity.NebsResponse;
import entity.QueryRequest;
import entity.constant.StringConstant;
import entity.system.LoginLog;
import entity.system.SystemUser;
import exception.NebsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.NebsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
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

	@GetMapping("check/{username}")
	@ControllerEndpoint(operation = "检查用户名", exceptionMessage = "检查用户名")
	public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
		return this.userService.findByName(username) == null;
	}

	@PostMapping("excel")
	@PreAuthorize("hasAuthority('user:export')")
	@ControllerEndpoint(operation = "导出用户数据", exceptionMessage = "导出Excel失败")
	public void export(QueryRequest queryRequest, SystemUser user, HttpServletResponse response) {
		List<SystemUser> users = this.userService.findUserDetailList(user, queryRequest).getRecords();
		ExcelKit.$Export(SystemUser.class, response).downXlsx(users, false);
	}

	@PutMapping("password/reset")
	@PreAuthorize("hasAuthority('user:reset')")
	@ControllerEndpoint(exceptionMessage = "重置用户密码失败")
	public void resetPassword(@NotBlank(message = "{required}") String usernames) {
		String[] usernameArr = usernames.split(StringConstant.COMMA);
		this.userService.resetPassword(usernameArr);
	}
}
