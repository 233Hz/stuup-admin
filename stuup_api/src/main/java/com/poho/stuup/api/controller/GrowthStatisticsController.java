package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.GrowthStatisticsVO;
import com.poho.stuup.service.GrowthStatisticsService;
import com.poho.stuup.util.ProjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author BUNGA
 * @date 2023/9/12 15:20
 */
@RestController
@RequestMapping("/growthStatistics")
@RequiredArgsConstructor
public class GrowthStatisticsController {

    private final HttpServletRequest request;

    private final GrowthStatisticsService growthStatisticsService;

    @GetMapping("/list")
    public ResponseModel<List<GrowthStatisticsVO>> list(@RequestParam("yearId") Long yearId, @RequestParam("semesterId") Long semesterId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        return ResponseModel.ok(growthStatisticsService.list(Long.valueOf(userId), yearId, semesterId));
    }
}
