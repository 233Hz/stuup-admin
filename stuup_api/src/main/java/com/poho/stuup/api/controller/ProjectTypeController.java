package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.service.IProjectTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "项目类型相关接口")
@RestController
@RequestMapping("/projectType")
public class ProjectTypeController {

    @Resource
    private IProjectTypeService projectTypeService;

    @ApiOperation(value = "获取项目", httpMethod = "GET")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return projectTypeService.findDataPageResult(key, page, pageSize);
    }
}
