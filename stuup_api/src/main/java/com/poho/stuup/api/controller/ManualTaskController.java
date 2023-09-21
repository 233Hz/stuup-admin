package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author BUNGA
 * @description: 手动触发定时任务
 * @date 2023/6/7 13:25
 */
@RestController
@RequestMapping("/manualTask")
public class ManualTaskController {

    @Resource
    private GrowScheduledTaskController growScheduledTaskController;

    @GetMapping("/task1")
    public ResponseModel task1() {
        growScheduledTaskController.calculateScoreForDay();
        return ResponseModel.ok();
    }

    @GetMapping("/task2")
    public ResponseModel task2() {
        growScheduledTaskController.calculateScoreForWeek();
        return ResponseModel.ok();
    }

    @GetMapping("/task3")
    public ResponseModel task3() {
        growScheduledTaskController.calculateScoreForMonth();
        return ResponseModel.ok();
    }

    @GetMapping("/task4")
    public ResponseModel task4() {
        growScheduledTaskController.calculateScoreForLastSemester();
        return ResponseModel.ok();
    }

    @GetMapping("/task5")
    public ResponseModel task5() {
        growScheduledTaskController.calculateScoreForYear();
        return ResponseModel.ok();
    }

    @GetMapping("/task6")
    public ResponseModel task6() {
        growScheduledTaskController.generateYearAndSemester();
        return ResponseModel.ok();
    }

    @GetMapping("/task7")
    public ResponseModel task8() {
        growScheduledTaskController.compensateCalculateFail();
        return ResponseModel.ok();
    }

    @GetMapping("/task8")
    public ResponseModel task9() {
        growScheduledTaskController.collectionTimeoutScore();
        return ResponseModel.ok();
    }

    @GetMapping("/task10")
    public boolean task10() {
        return growScheduledTaskController.getGrowthTimerTaskSwitch();
    }


}
