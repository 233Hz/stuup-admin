package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.GrowthInfo;
import com.poho.stuup.service.GrowthInfoService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 成长项目表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/growth")
public class GrowthInfoController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GrowthInfoService growthInfoService;


    @GetMapping("/info")
    public ResponseModel<GrowthInfo> getGrowthInfo() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return growthInfoService.getInfo(Long.valueOf(userId));
    }


}
