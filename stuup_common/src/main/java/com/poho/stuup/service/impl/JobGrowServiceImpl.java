package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.TimePeriod;
import com.poho.stuup.service.CalculateScoreService;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.JobGrowService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-18
 */
@Slf4j
@Service
public class JobGrowServiceImpl extends ServiceImpl<JobGrowMapper, JobGrow> implements JobGrowService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private GrowthItemMapper growthItemMapper;

    @Resource
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private CalculateScoreService calculateScoreService;

    @Resource
    private JobGrowStudentMapper jobGrowStudentMapper;

    @Resource
    private IConfigService configService;

    public static void main(String[] args) {
        System.out.println(DateUtil.endOfDay(DateUtil.endOfDay(new Date())));
    }

    @Override
    public void executeGrowJob(PeriodEnum periodEnum, Date date) {
        log.info("========================开始========================");
        long start = System.currentTimeMillis();
        Year year = yearMapper.findByRange(date);
        Semester semester = semesterMapper.findByRange(date);
        TimePeriod dateTimePeriod = Utils.getDateTimePeriod(periodEnum, date);

        int periodEnumValue = periodEnum.getValue();

        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery().eq(GrowthItem::getScorePeriod, periodEnumValue));
        for (GrowthItem growthItem : growthItems) {
            StrBuilder error = new StrBuilder();
            if (year == null) error.append("[未查询到当前学年]");
            if (semester == null) error.append("[未查询到当前学期]");
            if (dateTimePeriod.getStartTime() == null || dateTimePeriod.getEndTime() == null)
                error.append("[上下学期起止时间配置错误或者不存在，无法获取对应的起止时间]");

            JobGrow jobGrow = new JobGrow();
            jobGrow.setGrowId(growthItem.getId());
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            jobGrow.setExecDate(localDate);
            if (error.length() > 0) {
                jobGrow.setError(error.toString());
                baseMapper.insert(jobGrow);
                continue;
            }
            assert year != null;
            assert semester != null;
            baseMapper.insert(jobGrow);
            List<RecDefault> recDefaults = recDefaultMapper.selectList(Wrappers.<RecDefault>lambdaQuery()
                    .select(RecDefault::getStudentId)
                    .eq(RecDefault::getGrowId, growthItem.getId())
                    .between(RecDefault::getCreateTime, dateTimePeriod.getStartTime(), dateTimePeriod.getEndTime()));
            Map<Long, Long> countIdOccurrences = recDefaults.stream().collect(Collectors.groupingBy(RecDefault::getStudentId, Collectors.counting()));

            if (growthItem.getCalculateType() == CalculateTypeEnum.PLUS.getValue()) {
                for (Map.Entry<Long, Long> entry : countIdOccurrences.entrySet()) {
                    Long studentId = entry.getKey();
                    Long count = entry.getValue();
                    calculateScoreService.saveAddScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, jobGrow.getId(), count, growthItem.getScore(), growthItem.getScoreUpperLimit());
                }
            } else {
                List<Long> allStudentIds = studentMapper.selectIdList();
                Set<Long> studentIdSet = countIdOccurrences.keySet();
                for (Long studentId : allStudentIds) {
                    if (studentIdSet.contains(studentId)) {
                        Long count = countIdOccurrences.get(studentId);
                        calculateScoreService.saveDeductScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, jobGrow.getId(), count, growthItem.getScore(), growthItem.getScoreUpperLimit());
                    } else {
                        BigDecimal addScore = growthItem.getScoreUpperLimit();
                        calculateScoreService.saveAddScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, jobGrow.getId(), addScore);
                    }
                }
            }
            this.update(Wrappers.<JobGrow>lambdaUpdate()
                    .set(JobGrow::getState, WhetherEnum.YES.getValue())
                    .eq(JobGrow::getId, jobGrow.getId()));
        }
        long end = System.currentTimeMillis();
        log.info("========================结束========================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    @Override
    public void executeGrowJobCompensation() {
        Map<PeriodEnum, Integer> map = new HashMap<>();
        map.put(PeriodEnum.DAY, 30);
        map.put(PeriodEnum.WEEK, 4);
        map.put(PeriodEnum.MONTH, 1);
        map.put(PeriodEnum.SEMESTER, 1);
        map.put(PeriodEnum.YEAR, 1);
        LocalDate today = LocalDate.now();
        java.time.Year currentYear = java.time.Year.now();
        Config config1 = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_START_TIME.getKey());
        Config config2 = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_END_TIME.getKey());
        Config config3 = configService.selectByPrimaryKey(ConfigKeyEnum.NEXT_SEMESTER_START_TIME.getKey());
        Config config4 = configService.selectByPrimaryKey(ConfigKeyEnum.NEXT_SEMESTER_END_TIME.getKey());
        for (Map.Entry<PeriodEnum, Integer> entry : map.entrySet()) {
            PeriodEnum period = entry.getKey();
            Integer periodValue = entry.getValue();
            List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery().eq(GrowthItem::getScorePeriod, period.getValue()));
            for (int i = 1; i <= periodValue; i++) {
                LocalDate localDate;
                switch (period) {
                    case DAY:
                        localDate = today.minusDays(i);
                        break;
                    case WEEK:
                        localDate = today.minusWeeks(i);
                        break;
                    case MONTH:
                        localDate = today.minusMonths(i);
                        break;
                    case SEMESTER:
                        if (config1.getConfigValue() == null || config2.getConfigValue() == null || config3.getConfigValue() == null || config4.getConfigValue() == null)
                            continue;
                        LocalDate localDate1 = LocalDate.of(currentYear.getValue(), Integer.parseInt(config2.getConfigValue().substring(0, 2)), Integer.parseInt(config2.getConfigValue().substring(3)));
                        LocalDate localDate2 = LocalDate.of(currentYear.getValue(), Integer.parseInt(config4.getConfigValue().substring(0, 2)), Integer.parseInt(config4.getConfigValue().substring(3)));
                        if (today.isBefore(localDate1)) {
                            localDate = LocalDate.of(currentYear.minusYears(1).getValue(), Integer.parseInt(config4.getConfigValue().substring(0, 2)), Integer.parseInt(config4.getConfigValue().substring(3)));
                        } else if (today.isBefore(localDate2)) {
                            localDate = LocalDate.of(currentYear.getValue(), Integer.parseInt(config2.getConfigValue().substring(0, 2)), Integer.parseInt(config2.getConfigValue().substring(3)));
                        } else {
                            localDate = LocalDate.of(currentYear.getValue(), Integer.parseInt(config4.getConfigValue().substring(0, 2)), Integer.parseInt(config4.getConfigValue().substring(3)));
                        }
                        break;
                    case YEAR:
                        localDate = today.minusYears(i);
                        break;
                    default:
                        continue;
                }
                LocalTime localTime = LocalTime.of(23, 0);
                LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Year year = yearMapper.findByRange(date);
                Semester semester = semesterMapper.findByRange(date);
                TimePeriod dateTimePeriod = Utils.getDateTimePeriod(period, date);
                for (GrowthItem growthItem : growthItems) {
                    JobGrow jobGrow = baseMapper.selectOne(Wrappers.<JobGrow>lambdaQuery()
                            .between(JobGrow::getExecDate, dateTimePeriod.getStartTime(), dateTimePeriod.getEndTime())
                            .eq(JobGrow::getGrowId, growthItem.getId()));
                    List<RecDefault> recDefaults = recDefaultMapper.selectList(Wrappers.<RecDefault>lambdaQuery()
                            .select(RecDefault::getStudentId)
                            .eq(RecDefault::getGrowId, growthItem.getId())
                            .between(RecDefault::getCreateTime, dateTimePeriod.getStartTime(), dateTimePeriod.getEndTime()));
                    List<Long> defaultStudentIds = recDefaults.stream().map(RecDefault::getStudentId).collect(Collectors.toList());
                    StrBuilder error = new StrBuilder();
                    if (year == null) error.append("[未查询到当前学年]");
                    if (semester == null) error.append("[未查询到当前学期]");
                    if (dateTimePeriod.getStartTime() == null || dateTimePeriod.getEndTime() == null)
                        error.append("[上下学期起止时间配置错误或者不存在，无法获取对应的起止时间]");
                    if (jobGrow == null) {
                        // 未执行
                        JobGrow newJobGrow = new JobGrow();
                        newJobGrow.setGrowId(growthItem.getId());
                        newJobGrow.setExecDate(localDate);
                        if (error.length() > 0) {
                            newJobGrow.setError(error.toString());
                            baseMapper.insert(newJobGrow);
                            continue;
                        }
                        assert year != null;
                        assert semester != null;
                        baseMapper.insert(newJobGrow);
                        Map<Long, Long> countIdOccurrences = defaultStudentIds.stream().collect(Collectors.groupingBy(id -> id, Collectors.counting()));

                        if (growthItem.getCalculateType() == CalculateTypeEnum.PLUS.getValue()) {
                            for (Map.Entry<Long, Long> _entry : countIdOccurrences.entrySet()) {
                                Long studentId = _entry.getKey();
                                Long count = _entry.getValue();
                                calculateScoreService.saveAddScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, newJobGrow.getId(), count, growthItem.getScore(), growthItem.getScoreUpperLimit());
                            }
                        } else {
                            List<Long> allStudentIds = studentMapper.selectIdList();

                            Set<Long> studentIdSet = countIdOccurrences.keySet();
                            for (Long studentId : allStudentIds) {
                                if (studentIdSet.contains(studentId)) {
                                    Long count = countIdOccurrences.get(studentId);
                                    calculateScoreService.saveDeductScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, newJobGrow.getId(), count, growthItem.getScore(), growthItem.getScoreUpperLimit());
                                } else {
                                    BigDecimal addScore = growthItem.getScoreUpperLimit();
                                    calculateScoreService.saveAddScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, newJobGrow.getId(), addScore);
                                }
                            }
                        }
                        this.update(Wrappers.<JobGrow>lambdaUpdate()
                                .set(JobGrow::getState, WhetherEnum.YES.getValue())
                                .eq(JobGrow::getId, newJobGrow.getId()));
                    } else if (WhetherEnum.YES.getValue() != jobGrow.getState()) {
                        // 执行到一半中断
                        if (error.length() > 0) {
                            jobGrow.setError(error.toString());
                            baseMapper.updateById(jobGrow);
                        }
                        assert year != null;
                        assert semester != null;

                        List<JobGrowStudent> jobGrowStudents = jobGrowStudentMapper.selectList(Wrappers.<JobGrowStudent>lambdaQuery().select(JobGrowStudent::getStudentId).eq(JobGrowStudent::getJobGrowId, jobGrow.getId()));
                        Set<Long> jobIdSet = jobGrowStudents.stream().map(JobGrowStudent::getStudentId).collect(Collectors.toSet());
                        Map<Long, Long> countIdOccurrences = defaultStudentIds.stream().collect(Collectors.groupingBy(id -> id, Collectors.counting()));

                        Integer calculateType = growthItem.getCalculateType();
                        if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
                            for (Map.Entry<Long, Long> _entry : countIdOccurrences.entrySet()) {
                                Long studentId = _entry.getKey();
                                Long count = _entry.getValue();
                                if (!jobIdSet.contains(studentId)) {
                                    calculateScoreService.saveAddScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, jobGrow.getId(), count, growthItem.getScore(), growthItem.getScoreUpperLimit());
                                }
                            }
                        } else {
                            List<Long> allStudentIds = studentMapper.selectIdList();
                            Set<Long> studentIdSet = countIdOccurrences.keySet();
                            for (Long studentId : allStudentIds) {
                                if (!jobIdSet.contains(studentId)) {
                                    if (studentIdSet.contains(studentId)) {
                                        Long count = countIdOccurrences.get(studentId);
                                        calculateScoreService.saveDeductScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, jobGrow.getId(), count, growthItem.getScore(), growthItem.getScoreUpperLimit());
                                    } else {
                                        BigDecimal addScore = growthItem.getScoreUpperLimit();
                                        calculateScoreService.saveAddScoreCalculateResult(year.getOid(), semester.getId(), date, growthItem.getId(), studentId, jobGrow.getId(), addScore);
                                    }
                                }
                            }

                        }
                        this.update(Wrappers.<JobGrow>lambdaUpdate()
                                .set(JobGrow::getState, WhetherEnum.YES.getValue())
                                .eq(JobGrow::getId, jobGrow.getId()));
                    }
                }
            }
        }
    }
}

