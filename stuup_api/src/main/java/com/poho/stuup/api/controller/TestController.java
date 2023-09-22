package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private final GrowthItemService growthItemService;

    private final RecDefaultService recDefaultService;

    private final IStudentService studentService;

    private final IYearService yearService;

    private final SemesterService semesterService;

    private final TestService testService;


    @PostMapping("/山东菏泽曹县牛皮666我的宝贝")
    public ResponseModel<String> executeCalculateScore(@RequestBody Map<String, Object> param) {
        Long growthItemId = Long.parseLong(param.get("growthItemId").toString());
        Long yearId, semesterId;
        if (param.get("yearId") == null) {
            yearId = yearService.getCurrentYearId();
            if (yearId == null) return ResponseModel.failed("当前年份未设置，无法计算");
        } else {
            yearId = Long.parseLong(param.get("yearId").toString());
        }
        if (param.get("semesterId") == null) {
            semesterId = semesterService.getCurrentSemesterId();
            if (semesterId == null) return ResponseModel.failed("当前年份未设置，无法计算");
        } else {
            semesterId = Long.parseLong(param.get("semesterId").toString());
        }
        GrowthItem growthItem = growthItemService.getById(growthItemId);
        if (growthItem != null) {
            Integer calculateType = growthItem.getCalculateType();
            BigDecimal score = growthItem.getScore();
            BigDecimal scoreUpperLimit = growthItem.getScoreUpperLimit();
            List<Long> defaultStudentIds = recDefaultService.listObjs(Wrappers.<RecDefault>lambdaQuery()
                    .select(RecDefault::getStudentId)
                    .eq(RecDefault::getGrowId, growthItemId), id -> (Long) id);
            if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
                for (Long studentId : defaultStudentIds) {
                    testService.saveResult(studentId, growthItemId, yearId, semesterId, score);
                }
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
                List<Integer> allStudentId = studentService.getAllStudentId();
                Map<Long, Integer> studentCount = new HashMap<>();
                for (Long studentId : defaultStudentIds) {
                    Integer count = studentCount.getOrDefault(studentId, 0);
                    studentCount.put(studentId, ++count);
                }
                for (Integer studentId : allStudentId) {
                    if (defaultStudentIds.contains(studentId.longValue())) {
                        Integer count = studentCount.get(studentId.longValue());
                        testService.saveResult(studentId.longValue(), growthItemId, yearId, semesterId, score, scoreUpperLimit, count);
                    } else {
                        testService.saveResult(studentId.longValue(), growthItemId, yearId, semesterId, scoreUpperLimit);
                    }
                }
            }
        }

        return ResponseModel.ok("完成计算");
    }

}
