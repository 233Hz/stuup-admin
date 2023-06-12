package com.poho.stuup.api.controller;

import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.event.EventPublish;
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
    private EventPublish eventPublish;

    @Resource
    private RecScoreService recScoreService;

    /**
     * 计算每天任务分数
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void calculateScoreForDay() {
        recScoreService.calculateScore(PeriodEnum.DAY);
    }

    /**
     * 计算每周任务分数
     */
    @Scheduled(cron = "0 59 23 ? * SUN")
    public void calculateScoreForWeek() {
        recScoreService.calculateScore(PeriodEnum.WEEK);
    }

    /**
     * 计算每月任务分数
     */
    @Scheduled(cron = "0 59 23 28-31 * ?")
    public void calculateScoreForMonth() {
        recScoreService.calculateScore(PeriodEnum.MONTH);
    }

    /**
     * 计算每学期任务分数
     */
    @Scheduled(cron = "0 59 23 15 1,6 ? ")
    public void calculateScoreForSemester() {
        recScoreService.calculateScore(PeriodEnum.SEMESTER);
    }

    /**
     * 计算每年任务分数
     */
    @Scheduled(cron = "0 59 23 30 6 ?")
    public void calculateScoreForYear() {
        recScoreService.calculateScore(PeriodEnum.YEAR);
    }

    /**
     * 计算每三年任务分数
     */
//    @Scheduled(cron = "00 59 23 30 6 ? */3")
//    public void calculateScoreForThreeYear() {
//
//    }
//    @Scheduled(cron = "*/5 * * * * *")
//    public void test() {
//        eventPublish.publishEvent(new MonthRankingEvent(new Date()));
//    }

}
