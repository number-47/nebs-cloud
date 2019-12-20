package com.number47.nebs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.system.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
	List<Menu> findUserPermissions(String username);
}