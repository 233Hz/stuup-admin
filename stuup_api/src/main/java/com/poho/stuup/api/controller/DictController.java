package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.util.ProjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "字典信息相关接口")
@RestController
@RequestMapping("/dict")
public class DictController {

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "级别字典", httpMethod = "GET")
    @GetMapping("/level")
    public ResponseModel level() {
        return ResponseModel.newSuccessData(ProjectUtil.LEVEL_DICT_MAP.entrySet());
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "军训等级字典", httpMethod = "GET")
    @GetMapping("/militaryLevel")
    public ResponseModel militaryLevel() {
        return ResponseModel.newSuccessData(ProjectUtil.MILITARY_LEVEL_DICT_MAP.entrySet());
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "党团活动等级字典", httpMethod = "GET")
    @GetMapping("/politicalLevel")
    public ResponseModel politicalLevel() {
        return ResponseModel.newSuccessData(ProjectUtil.POLITICAL_LEVEL_DICT_MAP.entrySet());
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "学生状态字典", httpMethod = "GET")
    @GetMapping("/stuStatus")
    public ResponseModel stuStatus() {
        return ResponseModel.newSuccessData(ProjectUtil.STU_STATUS_DICT_MAP.entrySet());
    }


}
