package com.number47.nebs.server.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import entity.system.Column;
import entity.system.Table;
import entity.QueryRequest;

import java.util.List;

/**
 * @author MrBird
 */
public interface IGeneratorService {

    List<String> getDatabases(String databaseType);

    IPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName);

    List<Column> getColumns(String databaseType, String schemaName, String tableName);
}
