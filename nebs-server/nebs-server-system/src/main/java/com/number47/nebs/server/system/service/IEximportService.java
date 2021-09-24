package com.number47.nebs.server.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import entity.QueryRequest;

import java.util.List;

/**
 * @author MrBird
 */
public interface IEximportService extends IService<entity.system.Eximport> {
    /**
     * 查询（分页）
     *
     * @param request  QueryRequest
     * @param eximport eximport
     * @return IPage<Eximport>
     */
    IPage<entity.system.Eximport> findEximports(QueryRequest request, entity.system.Eximport eximport);


    /**
     * 批量插入
     *
     * @param list List<Eximport>
     */
    void batchInsert(List<entity.system.Eximport> list);

}
