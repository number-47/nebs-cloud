package com.number47.nebs.server.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import entity.QueryRequest;
import entity.system.Dept;

import java.util.List;
import java.util.Map;

public interface IDeptService extends IService<Dept> {

    Map<String, Object> findDepts(QueryRequest request, Dept dept);

    List<Dept> findDepts(Dept dept, QueryRequest request);

    void createDept(Dept dept);

    void updateDept(Dept dept);

    void deleteDepts(String[] deptIds);
}
