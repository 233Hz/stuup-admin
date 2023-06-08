package com.poho.stuup.api.controller;

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
    public void task1() {
        growScheduledTaskController.calculateScoreForDay();
    }

    @GetMapping("/task2")
    public void task2() {
        growScheduledTaskController.calculateScoreForWeek();
    }

    @GetMapping("/task3")
    public void task3() {
        growScheduledTaskController.calculateScoreForMonth();
    }

    @GetMapping("/task4")
    public void task4() {
        growScheduledTaskController.calculateScoreForSemester();
    }

    @GetMapping("/task5")
    public void task5() {
        growScheduledTaskController.calculateScoreForYear();
    }


}
