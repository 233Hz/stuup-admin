package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.AllKindsOfCompetitionAwardNumVO;
import com.poho.stuup.model.vo.GrowthScoreCountVO;
import com.poho.stuup.model.vo.StudentGrowthMonitorVO;
import com.poho.stuup.model.vo.YearAtSchoolNumVO;
import com.poho.stuup.service.VisualService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/visual")
public class VisualController {

    @Resource
    private VisualService visualService;

    @GetMapping("/studentGrowthMonitor")
    public ResponseModel<List<StudentGrowthMonitorVO>> studentGrowthMonitor() {
        return ResponseModel.ok(visualService.studentGrowthMonitor());
    }

    @GetMapping("/countNear3YearsAtSchoolNum")
    public ResponseModel<List<YearAtSchoolNumVO>> countNear3YearsAtSchoolNum() {
        return ResponseModel.ok(visualService.countNear3YearsAtSchoolNum());
    }

    @GetMapping("/countAllKindsOfCompetitionAwardNum")
    public ResponseModel<List<AllKindsOfCompetitionAwardNumVO>> countAllKindsOfCompetitionAwardNum() {
        return ResponseModel.ok(visualService.countAllKindsOfCompetitionAwardNum());
    }

    @GetMapping("/countScholarshipNum")
    public ResponseModel<Long> countScholarshipNum() {
        return ResponseModel.ok(visualService.countScholarshipNum());
    }

    @GetMapping("/countHoldAnActivityNum")
    public ResponseModel<Long> countHoldAnActivityNum() {
        return ResponseModel.ok(visualService.countHoldAnActivityNum());
    }

    @GetMapping("/countGrowthScoreForLastMonth")
    public ResponseModel<List<GrowthScoreCountVO>> countGrowthScoreForLastMonth() {
        return ResponseModel.ok(visualService.countGrowthScoreForLastMonth());
    }

}
