package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.ScreenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/screen")
public class ScreenController {

    @Resource
    private ScreenService screenService;

    @GetMapping("/countMajorPopulations")
    public ResponseModel<List<MajorPopulationsVO>> countMajorPopulations() {
        return ResponseModel.ok(screenService.countMajorPopulations());
    }

    @GetMapping("/important/data")
    public ResponseModel<ScreenImportantDataVO> getImportantData() {
        return ResponseModel.ok(screenService.getImportantData());
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

}
