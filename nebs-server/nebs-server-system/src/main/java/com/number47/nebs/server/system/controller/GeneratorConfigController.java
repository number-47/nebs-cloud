package com.number47.nebs.server.system.controller;


import annotation.ControllerEndpoint;
import com.number47.nebs.server.system.service.IGeneratorConfigService;
import entity.NebsResponse;
import entity.system.GeneratorConfig;
import exception.NebsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("generatorConfig")
public class GeneratorConfigController {

    @Autowired
    private IGeneratorConfigService generatorConfigService;

    @GetMapping
    @PreAuthorize("hasAuthority('gen:config')")
    public NebsResponse getGeneratorConfig() {
        return new NebsResponse().data(generatorConfigService.findGeneratorConfig());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gen:config:update')")
    @ControllerEndpoint(operation = "修改生成代码配置", exceptionMessage = "修改GeneratorConfig失败")
    public void updateGeneratorConfig(@Valid GeneratorConfig generatorConfig) throws NebsException {
        if (StringUtils.isBlank(generatorConfig.getId()))
            throw new NebsException("配置id不能为空");
        this.generatorConfigService.updateGeneratorConfig(generatorConfig);
    }
}
