package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.User;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.IStudentService;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.service.PortraitService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author BUNGA
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
    public ResponseModel<PortraitBasicInfoVO> getBasicInfo(@RequestParam("studentId") Integer studentId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return ResponseModel.ok(portraitService.getBasicInfo(student, user));
    }

    @GetMapping("/capacityEvaluator")
    public ResponseModel<List<PortraitCapacityEvaluatorVO>> getCapacityEvaluator(@RequestParam("studentId") Integer studentId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return ResponseModel.ok(portraitService.getCapacityEvaluator(Long.valueOf(studentId)));
    }

    @GetMapping("/awardRecord")
    public ResponseModel<List<PortraitAwardRecordVO>> getAwardRecord(@RequestParam("studentId") Integer studentId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return ResponseModel.ok(portraitService.getAwardRecord(Long.valueOf(studentId)));
    }

    @GetMapping("/activityRecord")
    public ResponseModel<List<PortraitActivityRecordVO>> getActivityRecord(@RequestParam("studentId") Integer studentId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return ResponseModel.ok(portraitService.getActivityRecord(Long.valueOf(studentId)));
    }

    @GetMapping("/rankingCurve")
    public ResponseModel<List<PortraitRankingCurveVO>> getRankingCurve(@RequestParam("studentId") Integer studentId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return portraitService.getRankingCurve(student);
    }

    @GetMapping("/growthAnalysis")
    public ResponseModel<List<PortraitGrowthAnalysisVO>> getGrowthAnalysis(@RequestParam("studentId") Integer studentId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return portraitService.getGrowthAnalysis(student);
    }

    @GetMapping("/growthData")
    public ResponseModel<PortraitGrowthDataVO> getGrowthData(@RequestParam("studentId") Integer studentId, @RequestParam("semesterId") Long semesterId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return ResponseModel.ok(portraitService.getGrowthData(studentId, semesterId));
    }

    @GetMapping("/growthComparison")
    public ResponseModel<List<PortraitGrowthComparisonVO>> getGrowthComparison(@RequestParam("studentId") Integer studentId, @RequestParam("semesterId") Long semesterId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return ResponseModel.ok(portraitService.getGrowthComparison(Long.valueOf(studentId), semesterId));
    }

    @GetMapping("/studyGrade")
    public ResponseModel<List<PortraitStudyGradeVO>> getStudyGrade(@RequestParam("studentId") Integer studentId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return portraitService.getStudyGrade(student);
    }

    @GetMapping("/studyCourse")
    public ResponseModel<List<PortraitStudyCourseVO>> getStudyCourse(@RequestParam("studentId") Integer studentId, @RequestParam("semesterId") Long semesterId) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到用户信息");
        Student student = studentService.selectByPrimaryKey(studentId);
        if (student == null) return ResponseModel.failed("未查询到学生信息");
        if (user.getUserType() == UserTypeEnum.STUDENT.getValue() && !student.getStudentNo().equals(user.getLoginName())) {
            return ResponseModel.failed("学生只能查询本人信息");
        }
        return portraitService.getStudyCourse(Long.valueOf(studentId), semesterId);
    }
}
