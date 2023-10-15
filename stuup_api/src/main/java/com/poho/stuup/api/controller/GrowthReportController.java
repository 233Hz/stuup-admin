package com.poho.stuup.api.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.GrowthReportVO;
import com.poho.stuup.service.GrowthReportService;
import com.poho.stuup.service.impl.StudentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/growth/report")
public class GrowthReportController {

    private final StudentServiceImpl studentService;
    private final GrowthReportService growthReportService;

    /**
     * 查询成长报告基本信息
     */
    @GetMapping("/basicInfo")
    public ResponseModel<GrowthReportVO.BasicInfo> queryGrowthReportBasicInfo(@RequestParam(name = "studentId", required = false) Integer studentId) {
        studentId = isNullStudentId(studentId);
        if (studentId != null) return ResponseModel.ok(growthReportService.getReportBasicInfo(studentId));
        return ResponseModel.ok();
    }

    /**
     * 查询成长报告道德与公民素养
     */
    @GetMapping("/ethicsAndCitizenship")
    public ResponseModel<GrowthReportVO.EthicsAndCitizenship> queryGrowthReportEthicsAndCitizenship(@RequestParam(name = "studentId", required = false) Integer studentId) {
        studentId = isNullStudentId(studentId);
        if (studentId != null) return ResponseModel.ok(growthReportService.getReportEthicsAndCitizenship(studentId));
        return ResponseModel.ok();
    }

    /**
     * 查询成长报告技能与学习素养
     */
    @GetMapping("/skillsAndLearningLiteracy")
    public ResponseModel<GrowthReportVO.SkillsAndLearningLiteracy> queryGrowthReportSkillsAndLearningLiteracy(@RequestParam(name = "studentId", required = false) Integer studentId) {
        studentId = isNullStudentId(studentId);
        if (studentId != null)
            return ResponseModel.ok(growthReportService.getReportSkillsAndLearningLiteracy(studentId));
        return ResponseModel.ok();
    }

    /**
     * 查询成长运动与身心健康
     */
    @GetMapping("/exerciseAndPhysicalAndMentalHealth")
    public ResponseModel<GrowthReportVO.ExerciseAndPhysicalAndMentalHealth> queryGrowthReportExerciseAndPhysicalAndMentalHealth(@RequestParam(name = "studentId", required = false) Integer studentId) {
        studentId = isNullStudentId(studentId);
        if (studentId != null)
            return ResponseModel.ok(growthReportService.getReportExerciseAndPhysicalAndMentalHealth(studentId));
        return ResponseModel.ok();
    }

    /**
     * 查询成长报告审美与艺术修养
     */
    @GetMapping("/aestheticAndArtisticAccomplishment")
    public ResponseModel<GrowthReportVO.AestheticAndArtisticAccomplishment> queryGrowthReportAestheticAndArtisticAccomplishment(@RequestParam(name = "studentId", required = false) Integer studentId) {
        studentId = isNullStudentId(studentId);
        if (studentId != null)
            return ResponseModel.ok(growthReportService.getReportAestheticAndArtisticAccomplishment(studentId));
        return ResponseModel.ok();
    }

    /**
     * 查询成长报告劳动与职业素养
     */
    @GetMapping("/laborAndProfessionalism")
    public ResponseModel<GrowthReportVO.LaborAndProfessionalism> queryGrowthReportLaborAndProfessionalism(@RequestParam(name = "studentId", required = false) Integer studentId) {
        studentId = isNullStudentId(studentId);
        if (studentId != null) return ResponseModel.ok(growthReportService.getReportLaborAndProfessionalism(studentId));
        return ResponseModel.ok();
    }

    private Integer isNullStudentId(Integer studentId) {
        if (studentId == null) {
            Long userId = Long.valueOf(StpUtil.getLoginId().toString());
            return studentService.getStudentIdByUserId(userId);
        }
        return studentId;
    }

}
