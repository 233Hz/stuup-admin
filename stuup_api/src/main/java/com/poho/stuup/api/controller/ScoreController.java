package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.ContestSearchDTO;
import com.poho.stuup.model.dto.ScoreDetailSearchDTO;
import com.poho.stuup.model.dto.ScoreSearchDTO;
import com.poho.stuup.service.IScoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "学生积分相关接口")
@RestController
@RequestMapping("/score")
public class ScoreController {

    @Resource
    private IScoreService scoreService;

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "获取总积分列表", httpMethod = "GET")
    @GetMapping("/listScore")
    public ResponseModel list(ScoreSearchDTO searchDTO) {

        return scoreService.findScorePageResult(searchDTO);
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "获取积分明细列表", httpMethod = "GET")
    @GetMapping("/listScoreDetail")
    public ResponseModel list(ScoreDetailSearchDTO searchDTO) {

        return scoreService.findScoreDetailPageResult(searchDTO);
    }
}
