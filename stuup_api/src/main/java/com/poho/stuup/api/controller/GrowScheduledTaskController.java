package com.poho.stuup.api.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.CommonConstants;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.model.Config;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Year;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.IYearService;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

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

    @Resource
    private IConfigService configService;

    @Resource
    private IYearService yearService;

    @Resource
    private SemesterService semesterService;

    /**
     * 每天检查生成当前学年和学期
     */
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void generateYearAndSemester() {
        Config config1 = configService.selectByPrimaryKey(CommonConstants.ConfigKey.LAST_SEMESTER_START_TIME.getKey());
        Config config2 = configService.selectByPrimaryKey(CommonConstants.ConfigKey.LAST_SEMESTER_END_TIME.getKey());
        Config config3 = configService.selectByPrimaryKey(CommonConstants.ConfigKey.NEXT_SEMESTER_START_TIME.getKey());
        Config config4 = configService.selectByPrimaryKey(CommonConstants.ConfigKey.NEXT_SEMESTER_END_TIME.getKey());
        if (config1 == null || config2 == null || config3 == null || config4 == null)
            throw new RuntimeException("系统配置不完整-上下学期起止时间，请联系管理员");
        Date date = new Date();
        int schoolYear = DateUtil.year(date);   //当前学年
        if (date.before(DateUtil.parseDate(StrUtil.format("{}-{}", schoolYear, config1.getConfigValue()))))
            schoolYear -= 1;
        // 上学期开始时间（当前学年开始时间）
        Date lastSemesterStartTime = DateUtil.parseDate(StrUtil.format("{}-{}", schoolYear, config1.getConfigValue()));
        // 下学期结束时间（当前学年结束时间）
        Date nextSemesterEndTime = DateUtil.parseDate(StrUtil.format("{}-{}", schoolYear + 1, config4.getConfigValue()));

        // 查询开始时间为 lastSemesterStartTime 结束时间为 nextSemesterEndTime 的学年是否存在，不存在则创建
        Year year = yearService.findYearForStartAndEndTime(lastSemesterStartTime, nextSemesterEndTime);
        if (year == null) {
            year = new Year();
            year.setYearName(StrUtil.format("{}学年", schoolYear));
            year.setYearStart(lastSemesterStartTime);
            year.setYearEnd(nextSemesterEndTime);
            yearService.insertSelective(year);
            year = yearService.findYearForStartAndEndTime(lastSemesterStartTime, nextSemesterEndTime);
        }
        // 上学期结束时间
        Date lastSemesterEndTime = DateUtil.parseDate(StrUtil.format("{}-{}", schoolYear + 1, config2.getConfigValue()));
        // 下学期开始时间
        Date nextSemesterStartTime = DateUtil.parseDate(StrUtil.format("{}-{}", schoolYear + 1, config3.getConfigValue()));
        // 查询开始时间为 lastSemesterStartTime 结束时间为 lastSemesterEndTime 的学期是否存在，不存在则创建
        Semester lastSemester = semesterService.getOne(Wrappers.<Semester>lambdaQuery()
                .eq(Semester::getStartTime, lastSemesterStartTime)
                .eq(Semester::getEndTime, lastSemesterEndTime));
        if (lastSemester == null) {
            lastSemester = new Semester();
            lastSemester.setYearId(year.getOid());
            lastSemester.setName(StrUtil.format("{}学年上学期", schoolYear));
            lastSemester.setStartTime(lastSemesterStartTime);
            lastSemester.setEndTime(lastSemesterEndTime);
            semesterService.save(lastSemester);
        }
        // 查询开始时间为 nextSemesterEndTime 结束时间为 nextSemesterStartTime 的学期是否存在，不存在则创建
        Semester nextSemester = semesterService.getOne(Wrappers.<Semester>lambdaQuery()
                .eq(Semester::getStartTime, nextSemesterStartTime)
                .eq(Semester::getEndTime, nextSemesterEndTime));
        if (nextSemester == null) {
            nextSemester = new Semester();
            nextSemester.setYearId(year.getOid());
            nextSemester.setName(StrUtil.format("{}学年下学期", schoolYear));
            nextSemester.setStartTime(nextSemesterStartTime);
            nextSemester.setEndTime(nextSemesterEndTime);
            semesterService.save(nextSemester);
        }
        // 最后设置当前学年和学期
        Year currentYear = yearService.findTimeBelongYear(date);
        yearService.setAllYearNotCurr();
        if (currentYear != null) {
            yearService.setCurrentYear(currentYear.getOid());
        }
        Semester currentSemester = semesterService.findTimeBelongYear(date);
        semesterService.update(Wrappers.<Semester>lambdaUpdate()
                .set(Semester::getIsCurrent, WhetherEnum.NO.getValue()));
        if (currentSemester != null) {
            semesterService.update(Wrappers.<Semester>lambdaUpdate()
                    .set(Semester::getIsCurrent, WhetherEnum.YES.getValue())
                    .eq(Semester::getId, currentSemester.getId()));
        }
    }

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
//        eventPublish.publishEvent(new StatisticsYearRankEvent(yearId));
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
//        eventPublish.publishEvent(new StatisticsMonthRankEvent(new Date()));
//    }

}
