package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.service.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 13:56 2020/9/3
 * @Modified By:
 */
@Api(tags = "基本配置相关接口")
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Resource
    private IConfigService configService;

    /**
     * 保存基本配置
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "保存基本配置", httpMethod = "POST")
    @RequestMapping(value = "/saveBase", method = RequestMethod.POST)
    public ResponseModel saveBase(String key, String value) {
        return configService.saveOrUpdate(key, value);
    }
}
