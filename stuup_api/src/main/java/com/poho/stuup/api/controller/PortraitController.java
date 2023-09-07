package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.IStudentService;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.service.PortraitService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/8/7 8:53
 */

@RestController
@RequestMapping("/portrait")
public class PortraitController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private PortraitService portraitService;

    @Resource
    private IUserService userService;

    @Resource
    private IStudentService studentService;

    @GetMapping("/basicInfo")
    public ResponseModel<PortraitBasicInfoVO> getBasicInfo() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getBasicInfo(Long.valueOf(userId));
    }

    @GetMapping("/capacityEvaluator")
    public ResponseModel<List<PortraitCapacityEvaluatorVO>> getCapacityEvaluator() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getCapacityEvaluator(Long.valueOf(userId));
    }

    @GetMapping("/awardRecord")
    public ResponseModel<List<PortraitAwardRecordVO>> getAwardRecord() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getAwardRecord(Long.valueOf(userId));
    }

    @GetMapping("/activityRecord")
    public ResponseModel<List<PortraitActivityRecordVO>> getActivityRecord() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getActivityRecord(Long.valueOf(userId));
    }

    @GetMapping("/rankingCurve")
    public ResponseModel<List<PortraitRankingCurveVO>> getRankingCurve() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getRankingCurve(Long.valueOf(userId));
    }

    @GetMapping("/growthAnalysis")
    public ResponseModel<List<PortraitGrowthAnalysisVO>> getGrowthAnalysis() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getGrowthAnalysis(Long.valueOf(userId));
    }

    @GetMapping("/growthData/{semesterId}")
    public ResponseModel<PortraitGrowthDataVO> getGrowthData(@PathVariable("semesterId") Long semesterId) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getGrowthData(Long.valueOf(userId), semesterId);
    }

    @GetMapping("/growthComparison")
    public ResponseModel<List<PortraitGrowthComparisonVO>> getGrowthComparison(@RequestParam("semesterId") Long semesterId, @RequestParam("studentId") Long studentId) {
        return ResponseModel.ok(portraitService.getGrowthComparison(semesterId, studentId));
    }

    @GetMapping("/studyGrade")
    public ResponseModel<List<PortraitStudyGradeVO>> getStudyGrade() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getStudyGrade(Long.valueOf(userId));
    }

    @GetMapping("/studyCourse/{semesterId}")
    public ResponseModel<List<PortraitStudyCourseVO>> getStudyCourse(@PathVariable("semesterId") Long semesterId) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return portraitService.getStudyCourse(Long.valueOf(userId), semesterId);
    }
}
