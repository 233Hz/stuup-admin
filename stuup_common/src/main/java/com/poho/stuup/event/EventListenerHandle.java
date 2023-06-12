package com.poho.stuup.event;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.model.RankingMonth;
import com.poho.stuup.model.RankingYear;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.dto.TimePeriod;
import com.poho.stuup.service.IStudentService;
import com.poho.stuup.service.RankingMonthService;
import com.poho.stuup.service.RankingYearService;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author BUNGA
 * @description: 事件监听器处理
 * @date 2023/6/12 14:35
 */

@Slf4j
@Component
public class EventListenerHandle {

    @Resource
    private RecScoreService recScoreService;

    @Resource
    private IStudentService studentService;

    @Resource
    private RankingYearService rankingYearService;

    @Resource
    private RankingMonthService rankingMonthService;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handle(YearRankingEvent event) {
        log.info("====================开始统计排行榜====================");
        long start = System.currentTimeMillis();
        Long yearId = event.getYearId();
        // 查询出当前所有记录
        List<RecScore> recScores = recScoreService.list(Wrappers.<RecScore>lambdaQuery()
                .select(RecScore::getStudentId, RecScore::getScore)
                .eq(RecScore::getYearId, yearId));

        // 按学生分组求和排序
        Map<Long, Integer> scoresByStudentId = recScores.stream()
                .collect(Collectors.groupingBy(RecScore::getStudentId, Collectors.summingInt(RecScore::getScore)));

        List<Map.Entry<Long, Integer>> sortedScores = scoresByStudentId.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<Long, Integer>::getValue).reversed())
                .collect(Collectors.toList());

        int ranking = 1;
        for (Map.Entry<Long, Integer> sortedScore : sortedScores) {
            RankingYear rankingYear = new RankingYear();
            rankingYear.setYearId(yearId);
            rankingYear.setRanking(ranking);
            rankingYear.setStudentId(sortedScore.getKey());
            rankingYear.setScore(sortedScore.getValue());
            rankingYearService.save(rankingYear);
            ranking++;
        }

        List<Long> notZeroStudentId = sortedScores.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        List<Integer> allStudentId = studentService.getAllStudentId().stream().filter(id -> !notZeroStudentId.contains(Long.parseLong(String.valueOf(id)))).collect(Collectors.toList());

        for (Integer studentId : allStudentId) {
            RankingYear rankingYear = new RankingYear();
            rankingYear.setYearId(yearId);
            rankingYear.setRanking(ranking);
            rankingYear.setStudentId(Long.valueOf(studentId));
            rankingYear.setScore(0);
            rankingYearService.save(rankingYear);
            ranking++;
        }

        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handle(MonthRankingEvent event) {
        Date now = event.getNow();
        log.info("====================开始统计排行榜====================");
        long start = System.currentTimeMillis();
        int year = DateUtil.year(now);
        int month = DateUtil.month(now);
        TimePeriod timePeriod = Utils.getCurrentTimePeriod(PeriodEnum.MONTH);
        Map<Long, Integer> timePeriodScoreMap = recScoreService.findTimePeriodScoreMap(timePeriod.getStartTime(), timePeriod.getEndTime());
        Map<Long, Integer> sortedMap = timePeriodScoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int ranking = 1;
        for (Map.Entry<Long, Integer> entry : sortedMap.entrySet()) {
            RankingMonth rankingMonth = new RankingMonth();
            rankingMonth.setRanking(ranking);
            rankingMonth.setYear(year);
            rankingMonth.setMonth(month);
            rankingMonth.setStudentId(entry.getKey());
            rankingMonth.setScore(entry.getValue());
            ranking++;
            rankingMonthService.save(rankingMonth);
        }
        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }
}
