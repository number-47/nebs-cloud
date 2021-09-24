package com.number47.nebs.server.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.number47.nebs.server.system.mapper.EximportMapper;
import com.number47.nebs.server.system.properties.NebsServerSystemProperties;
import com.number47.nebs.server.system.service.IEximportService;
import entity.QueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EximportServiceImpl extends ServiceImpl<EximportMapper, entity.system.Eximport> implements IEximportService {

    @Autowired
    private NebsServerSystemProperties properties;

    @Override
    public IPage<entity.system.Eximport> findEximports(QueryRequest request, entity.system.Eximport eximport) {
        Page<entity.system.Eximport> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, null);
    }

    @Override
    @Transactional
    public void batchInsert(List<entity.system.Eximport> list) {
        this.saveBatch(list, properties.getBatchInsertMaxNum());
    }

}
