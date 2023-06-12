package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.GrowGardenDTO;
import com.poho.stuup.model.vo.GrowGardenVO;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/garden")
public class GardenController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private StuScoreService stuScoreService;

    @GetMapping("/page")
    private ResponseModel<IPage<GrowGardenVO>> getGrowGarden(Page<GrowGardenVO> page, GrowGardenDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return stuScoreService.getGrowGarden(page, query, Long.valueOf(userId));
    }


}
