package com.number47.nebs.server.system.controller;


import annotation.ControllerEndpoint;
import com.number47.nebs.common.entity.constant.GeneratorConstant;
import entity.system.Column;
import com.number47.nebs.server.system.helper.GeneratorHelper;
import com.number47.nebs.server.system.service.IGeneratorConfigService;
import com.number47.nebs.server.system.service.IGeneratorService;
import entity.NebsResponse;
import entity.QueryRequest;
import entity.system.GeneratorConfig;
import exception.NebsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.FileUtil;
import util.NebsUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("generator")
public class GeneratorController {

    private static final String SUFFIX = "_code.zip";

    @Autowired
    private IGeneratorService generatorService;
    @Autowired
    private IGeneratorConfigService generatorConfigService;
    @Autowired
    private GeneratorHelper generatorHelper;

    @GetMapping("tables")
    @PreAuthorize("hasAuthority('gen:generate')")
    public NebsResponse tablesInfo(String tableName, QueryRequest request) {
        Map<String, Object> dataTable = NebsUtil.getDataTable(generatorService.getTables(tableName, request, GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME));
        return new NebsResponse().data(dataTable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gen:generate:gen')")
    @ControllerEndpoint(operation = "生成代码", exceptionMessage = "代码生成失败")
    public void generate(@NotBlank(message = "{required}") String name, String remark, HttpServletResponse response) throws Exception {
        GeneratorConfig generatorConfig = generatorConfigService.findGeneratorConfig();
        if (generatorConfig == null) {
            throw new NebsException("代码生成配置为空");
        }

        String className = name;
        if (GeneratorConfig.TRIM_YES.equals(generatorConfig.getIsTrim())) {
            className = RegExUtils.replaceFirst(name, generatorConfig.getTrimValue(), StringUtils.EMPTY);
        }

        generatorConfig.setTableName(name);
        generatorConfig.setClassName(NebsUtil.underscoreToCamel(className));
        generatorConfig.setTableComment(remark);
        // 生成代码到临时目录
        List<Column> columns = generatorService.getColumns(GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME, name);
        generatorHelper.generateEntityFile(columns, generatorConfig);
        generatorHelper.generateMapperFile(columns, generatorConfig);
        generatorHelper.generateMapperXmlFile(columns, generatorConfig);
        generatorHelper.generateServiceFile(columns, generatorConfig);
        generatorHelper.generateServiceImplFile(columns, generatorConfig);
        generatorHelper.generateControllerFile(columns, generatorConfig);
        // 打包
        String zipFile = System.currentTimeMillis() + SUFFIX;
        FileUtil.compress(GeneratorConstant.TEMP_PATH + "src", zipFile);
        // 下载
        FileUtil.download(zipFile, name + SUFFIX, true, response);
        // 删除临时目录
        FileUtil.delete(GeneratorConstant.TEMP_PATH);
    }
}
