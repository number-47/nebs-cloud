package com.number47.nebs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.system.SystemUser;

/**
 * @author number47
 * @date 2019/11/24 20:59
 * @description
 */
public interface UserMapper extends BaseMapper<SystemUser> {
	SystemUser findByName(String username);
}