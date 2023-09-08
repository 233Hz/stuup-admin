package com.poho.stuup.api.controller;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.api.config.PropertiesConfig;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.ScreenService;
import com.poho.stuup.service.SyncInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/screen")
public class ScreenController {

    @Resource
    private ScreenService screenService;

    @Resource
    private SyncInfoService syncInfoService;

    @Resource
    private PropertiesConfig propertiesConfig;

    @GetMapping("/countMajorPopulations")
    public ResponseModel<List<MajorPopulationsVO>> countMajorPopulations() {
        return ResponseModel.ok(screenService.countMajorPopulations());
    }

    @GetMapping("/important/data")
    public ResponseModel<ScreenImportantDataVO> getImportantData() {
        // 统计社团总数
        StopWatch stopWatch = new StopWatch("学生成长可视化大屏数据接口");
        stopWatch.start("社团总数统计");
        ResponseModel<Integer> remoteOpenCommunityTotal = syncInfoService.getRemoteOpenCommunityTotal(propertiesConfig.getCommunityUrl());
        stopWatch.stop();

        ScreenImportantDataVO importantData = screenService.getImportantData(stopWatch);
        log.info(StrUtil.format("可视化大屏数据接口花费时间：{}", stopWatch.prettyPrint(TimeUnit.SECONDS)));

        if (remoteOpenCommunityTotal.getCode() == 0 && remoteOpenCommunityTotal.getData() != null) {
            importantData.setClubNum(remoteOpenCommunityTotal.getData());
        }
        return ResponseModel.ok(importantData);
    }


    @GetMapping("/studentGrowthMonitor")
    public ResponseModel<List<StudentGrowthMonitorVO>> studentGrowthMonitor() {
        return ResponseModel.ok(screenService.studentGrowthMonitor());
    }

    @GetMapping("/countNear3YearsAtSchoolNum")
    public ResponseModel<List<YearAtSchoolNumVO>> countNear3YearsAtSchoolNum() {
        return ResponseModel.ok(screenService.countNear3YearsAtSchoolNum());
    }

    @GetMapping("/countAllKindsOfCompetitionAwardNum")
    public ResponseModel<List<AllKindsOfCompetitionAwardNumVO>> countAllKindsOfCompetitionAwardNum() {
        return ResponseModel.ok(screenService.countAllKindsOfCompetitionAwardNum());
    }

    @GetMapping("/countGrowthScoreCompare")
    public ResponseModel<List<GrowthScoreCountVO>> countGrowthScoreCompare() {
        return ResponseModel.ok(screenService.countGrowthScoreCompare());
    }

    @GetMapping("/countDailyVisits")
    public ResponseModel<List<DailyVisitsVO>> countDailyVisits() {
        return ResponseModel.ok(screenService.countDailyVisits());
    }

    @GetMapping("/countReviewOfEachClass")
    public ResponseModel<List<ReviewOfEachClassVO>> countReviewOfEachClass() {
        return ResponseModel.ok(screenService.countReviewOfEachClass());
    }


}
