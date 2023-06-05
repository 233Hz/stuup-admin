package com.poho.stuup.api.controller;

import com.poho.stuup.service.RecScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author BUNGA
 * @description: 计算成长积分定时脚本
 * @date 2023/6/5 15:44
 */
@Slf4j
@Component
public class GrowScheduledTaskController {

    @Resource
    private RecScoreService recScoreService;

//    /**
//     * 计算每天任务分数
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void calculateScoreForDay() {
//
//    }
//
//    /**
//     * 计算每周任务分数
//     */
//    @Scheduled(cron = "0 0 0 ? * SUN")
//    public void calculateScoreForWeek() {
//
//    }
//
//    /**
//     * 计算每月任务分数
//     */
//    @Scheduled(cron = "0 0 0 L * ?")
//    public void calculateScoreForMonth() {
//
//    }
//
//    /**
//     * 计算每学期任务分数
//     */
//    @Scheduled(cron = "0 0 0 15 1,6 ? ")
//    public void calculateScoreForSemester() {
//
//    }
//
//    /**
//     * 计算每年任务分数
//     */
//    @Scheduled(cron = "0 0 0 31 12 ?")
//    public void calculateScoreForYear() {
//
//    }
//
//    /**
//     * 计算每三年任务分数
//     */
//    @Scheduled(cron = "0 0 0 31 12 ? */3")
//    public void calculateScoreForThreeYear() {
//
//    }

}
