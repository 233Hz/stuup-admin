package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.model.Config;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.YearInfo;
import com.poho.stuup.service.*;
import com.poho.stuup.service.impl.StudentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private IConfigService configService;

    @Resource
    private IYearService yearService;

    @Resource
    private SemesterService semesterService;

    @Resource
    private StudentServiceImpl studentService;

    @Resource
    private YearInfoService yearInfoService;

    @Resource
    private JobGrowService jobGrowService;


    /**
     * 每天检查生成当前学年和学期
     */
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void generateYearAndSemester() {
        Config config1 = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_START_TIME.getKey());
        Config config2 = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_END_TIME.getKey());
        Config config3 = configService.selectByPrimaryKey(ConfigKeyEnum.NEXT_SEMESTER_START_TIME.getKey());
        Config config4 = configService.selectByPrimaryKey(ConfigKeyEnum.NEXT_SEMESTER_END_TIME.getKey());
        if (config1 == null || config2 == null || config3 == null || config4 == null)
            throw new RuntimeException("系统配置不完整-上下学期起止时间，请联系管理员");

        String configValue1 = config1.getConfigValue();
        String configValue2 = config2.getConfigValue();
        String configValue3 = config3.getConfigValue();
        String configValue4 = config4.getConfigValue();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime time1 = LocalDateTime.of(now.getYear(), Integer.parseInt(configValue2.substring(0, 2)), Integer.parseInt(configValue2.substring(3)), 23, 59, 59);
        LocalDateTime time2 = LocalDateTime.of(now.getYear(), Integer.parseInt(configValue4.substring(0, 2)), Integer.parseInt(configValue4.substring(3)), 23, 59, 59);

        LocalDateTime startTime1 = LocalDateTime.of(now.getYear() - 1, Integer.parseInt(configValue1.substring(0, 2)), Integer.parseInt(configValue1.substring(3)), 0, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(now.getYear(), Integer.parseInt(configValue2.substring(0, 2)), Integer.parseInt(configValue2.substring(3)), 23, 59, 59);
        LocalDateTime startTime3 = LocalDateTime.of(now.getYear(), Integer.parseInt(configValue3.substring(0, 2)), Integer.parseInt(configValue3.substring(3)), 0, 0, 0);
        LocalDateTime startTime4 = LocalDateTime.of(now.getYear(), Integer.parseInt(configValue4.substring(0, 2)), Integer.parseInt(configValue4.substring(3)), 23, 59, 59);
        LocalDateTime startTime5 = LocalDateTime.of(now.getYear(), Integer.parseInt(configValue1.substring(0, 2)), Integer.parseInt(configValue1.substring(3)), 0, 0, 0);
        LocalDateTime startTime6 = LocalDateTime.of(now.getYear() + 1, Integer.parseInt(configValue2.substring(0, 2)), Integer.parseInt(configValue2.substring(3)), 23, 59, 59);
        LocalDateTime startTime7 = LocalDateTime.of(now.getYear() + 1, Integer.parseInt(configValue3.substring(0, 2)), Integer.parseInt(configValue3.substring(3)), 0, 0, 0);
        LocalDateTime startTime8 = LocalDateTime.of(now.getYear() + 1, Integer.parseInt(configValue4.substring(0, 2)), Integer.parseInt(configValue4.substring(3)), 23, 59, 59);

        Year year;
        Semester semester;
        if (now.isBefore(time1) || now.isEqual(time1)) {
            // 当前处于上一学年上学期
            Date yearStart = Date.from(startTime1.atZone(ZoneId.systemDefault()).toInstant());
            Date yearEnd = Date.from(startTime4.atZone(ZoneId.systemDefault()).toInstant());
            year = yearService.findYearForStartAndEndTime(yearStart, yearEnd);
            if (year == null) {
                year = new Year();
                year.setYearName(StrUtil.format("{}学年", now.getYear() - 1));
                year.setYearStart(yearStart);
                year.setYearEnd(yearEnd);
                yearService.insertSelective(year);
                year = yearService.findYearForStartAndEndTime(yearStart, yearEnd);
            }
            Date semesterStart = Date.from(startTime1.atZone(ZoneId.systemDefault()).toInstant());
            Date semesterEnd = Date.from(startTime2.atZone(ZoneId.systemDefault()).toInstant());
            semester = semesterService.getOne(Wrappers.<Semester>lambdaQuery()
                    .eq(Semester::getStartTime, semesterStart)
                    .eq(Semester::getEndTime, semesterEnd));
            if (semester == null) {
                semester = new Semester();
                semester.setYearId(year.getOid());
                semester.setName(StrUtil.format("{}学年上学期", now.getYear() - 1));
                semester.setStartTime(semesterStart);
                semester.setEndTime(semesterEnd);
                semesterService.save(semester);
            }
        } else if (now.isBefore(time2) || now.isEqual(time2)) {
            // 当前处于上一学年下学期
            Date yearStart = Date.from(startTime1.atZone(ZoneId.systemDefault()).toInstant());
            Date yearEnd = Date.from(startTime4.atZone(ZoneId.systemDefault()).toInstant());
            year = yearService.findYearForStartAndEndTime(yearStart, yearEnd);
            if (year == null) {
                year = new Year();
                year.setYearName(StrUtil.format("{}学年", now.getYear() - 1));
                year.setYearStart(yearStart);
                year.setYearEnd(yearEnd);
                yearService.insertSelective(year);
                year = yearService.findYearForStartAndEndTime(yearStart, yearEnd);
            }
            Date semesterStart = Date.from(startTime3.atZone(ZoneId.systemDefault()).toInstant());
            Date semesterEnd = Date.from(startTime4.atZone(ZoneId.systemDefault()).toInstant());
            semester = semesterService.getOne(Wrappers.<Semester>lambdaQuery()
                    .eq(Semester::getStartTime, semesterStart)
                    .eq(Semester::getEndTime, semesterEnd));
            if (semester == null) {
                semester = new Semester();
                semester.setYearId(year.getOid());
                semester.setName(StrUtil.format("{}学年下学期", now.getYear() - 1));
                semester.setStartTime(semesterStart);
                semester.setEndTime(semesterEnd);
                semesterService.save(semester);
            }
        } else {
            //当前处于当前学年上学期
            Date yearStart = Date.from(startTime5.atZone(ZoneId.systemDefault()).toInstant());
            Date yearEnd = Date.from(startTime8.atZone(ZoneId.systemDefault()).toInstant());
            year = yearService.findYearForStartAndEndTime(yearStart, yearEnd);
            if (year == null) {
                year = new Year();
                year.setYearName(StrUtil.format("{}学年", now.getYear()));
                year.setYearStart(yearStart);
                year.setYearEnd(yearEnd);
                yearService.insertSelective(year);
                year = yearService.findYearForStartAndEndTime(yearStart, yearEnd);
            }
            Date semesterStart = Date.from(startTime5.atZone(ZoneId.systemDefault()).toInstant());
            Date semesterEnd = Date.from(startTime6.atZone(ZoneId.systemDefault()).toInstant());
            semester = semesterService.getOne(Wrappers.<Semester>lambdaQuery()
                    .eq(Semester::getStartTime, semesterStart)
                    .eq(Semester::getEndTime, semesterEnd));
            if (semester == null) {
                semester = new Semester();
                semester.setYearId(year.getOid());
                semester.setName(StrUtil.format("{}学年下学期", now.getYear()));
                semester.setStartTime(semesterStart);
                semester.setEndTime(semesterEnd);
                semesterService.save(semester);
            }
        }

        // 设置当前学年和当前学期
        yearService.setCurrentYear(year.getOid());
        semesterService.update(Wrappers.<Semester>lambdaUpdate()
                .set(Semester::getIsCurrent, WhetherEnum.YES.getValue())
                .eq(Semester::getId, semester.getId()));
        semesterService.update(Wrappers.<Semester>lambdaUpdate()
                .set(Semester::getIsCurrent, WhetherEnum.NO.getValue())
                .ne(Semester::getId, semester.getId()));

        // 查询当前在校生人数
        Integer countAtSchool = studentService.countAtSchoolNum();
        YearInfo yearInfo = yearInfoService.getOne(Wrappers.<YearInfo>lambdaQuery()
                .eq(YearInfo::getYearId, year.getOid()));
        // 设置当前在校生人数
        if (yearInfo == null) {
            yearInfo = new YearInfo();
            yearInfo.setYearId(year.getOid());
            yearInfo.setStudentNum(countAtSchool);
            yearInfoService.save(yearInfo);
        } else {
            yearInfoService.update(Wrappers.<YearInfo>lambdaUpdate()
                    .set(YearInfo::getStudentNum, countAtSchool)
                    .eq(YearInfo::getYearId, year.getOid()));
        }
    }

    /**
     * 计算每天任务分数
     */
    @Async
    @Scheduled(cron = "0 0 23 * * ?")
    public void calculateScoreForDay() {
        jobGrowService.executeGrowJob(PeriodEnum.DAY);
    }

    /**
     * 计算每周任务分数
     */
    @Async
    @Scheduled(cron = "0 0 23 ? * SUN")
    public void calculateScoreForWeek() {
        jobGrowService.executeGrowJob(PeriodEnum.WEEK);
    }

    /**
     * 计算每月任务分数
     */
    @Async
    @Scheduled(cron = "0 0 23 28-31 * ?")
    public void calculateScoreForMonth() {
        jobGrowService.executeGrowJob(PeriodEnum.MONTH);
    }

    /**
     * 计算每学期任务分数
     */
    @Async
    @Scheduled(cron = "0 0 23 15 1,6 ? ")
    public void calculateScoreForSemester() {
        jobGrowService.executeGrowJob(PeriodEnum.SEMESTER);
    }

    /**
     * 计算每年任务分数
     */
    @Async
    @Scheduled(cron = "0 0 23 30 6 ?")
    public void calculateScoreForYear() {
        jobGrowService.executeGrowJob(PeriodEnum.YEAR);
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void compensateCalculateFail() {
        jobGrowService.executeGrowJobCompensation();
    }


}
