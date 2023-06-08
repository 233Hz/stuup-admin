package com.poho.stuup.api.controller;

import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.service.RecScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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

    /**
     * 计算每天任务分数
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void calculateScoreForDay() {
        log.info("开始执行定时任务");
        long start = System.currentTimeMillis();
        recScoreService.calculateScore(PeriodEnum.DAY);
        long end = System.currentTimeMillis();
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    /**
     * 计算每周任务分数
     */
    @Scheduled(cron = "0 59 23 ? * SUN")
    public void calculateScoreForWeek() {
        log.info("开始执行定时任务");
        long start = System.currentTimeMillis();
        recScoreService.calculateScore(PeriodEnum.WEEK);
        long end = System.currentTimeMillis();
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    /**
     * 计算每月任务分数
     */
    @Scheduled(cron = "0 59 23 28-31 * ?")
    public void calculateScoreForMonth() {
        log.info("开始执行定时任务");
        long start = System.currentTimeMillis();
        recScoreService.calculateScore(PeriodEnum.MONTH);
        long end = System.currentTimeMillis();
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    /**
     * 计算每学期任务分数
     */
    @Scheduled(cron = "0 59 23 15 1,6 ? ")
    public void calculateScoreForSemester() {
        log.info("开始执行定时任务");
        long start = System.currentTimeMillis();
        recScoreService.calculateScore(PeriodEnum.SEMESTER);
        long end = System.currentTimeMillis();
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    /**
     * 计算每年任务分数
     */
    @Scheduled(cron = "0 59 23 31 12 ?")
    public void calculateScoreForYear() {
        log.info("开始执行定时任务");
        long start = System.currentTimeMillis();
        recScoreService.calculateScore(PeriodEnum.YEAR);
        long end = System.currentTimeMillis();
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    /**
     * 计算每三年任务分数
     */
//    @Scheduled(cron = "00 59 23 30 6 ? */3")
//    public void calculateScoreForThreeYear() {
//
//    }


}
