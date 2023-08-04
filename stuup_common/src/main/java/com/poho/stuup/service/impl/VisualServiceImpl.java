package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.ChangeTypeEnum;
import com.poho.stuup.constant.CommonConstants;
import com.poho.stuup.dao.GrowthMapper;
import com.poho.stuup.dao.YearInfoMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.*;
import com.poho.stuup.model.vo.AllKindsOfCompetitionAwardNumVO;
import com.poho.stuup.model.vo.GrowthScoreCountVO;
import com.poho.stuup.model.vo.StudentGrowthMonitorVO;
import com.poho.stuup.model.vo.YearAtSchoolNumVO;
import com.poho.stuup.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisualServiceImpl implements VisualService {

    private final static Map<String, String> COMPETITION_AWARD_LEVEL_OF_CONFIG_KEY_MAP = new HashMap<String, String>() {
        {
            put("国家级", CommonConstants.ConfigKey.NATIONAL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
            put("市级", CommonConstants.ConfigKey.CITY_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
            put("区级", CommonConstants.ConfigKey.DISTRICT_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
            put("校级", CommonConstants.ConfigKey.SCHOOL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
        }
    };
    @Resource
    private GrowthMapper growthMapper;
    @Resource
    private GrowthItemService growthItemService;
    @Resource
    private RecDefaultService recDefaultService;
    @Resource
    private YearInfoMapper yearInfoMapper;
    @Resource
    private IConfigService configService;
    @Resource
    private YearMapper yearMapper;
    @Resource
    private RecScoreService recScoreService;

    @Override
    public List<StudentGrowthMonitorVO> studentGrowthMonitor() {
        List<StudentGrowthMonitorVO> resultList = new ArrayList<>();

        // 查询扣分项
        List<GrowthItem> growthItems = growthItemService.list(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getName, GrowthItem::getId)
                .eq(GrowthItem::getCalculateType, CalculateTypeEnum.MINUS.getValue()));
        if (CollUtil.isNotEmpty(growthItems)) {
            Map<Long, String> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getId, GrowthItem::getName));
            growthItems.clear();
            // 获取本月起止时间
            Date date = new Date();
            DateTime startTime = DateUtil.beginOfMonth(date);
            DateTime endTime = DateUtil.endOfMonth(date);
            // 查询时间段内的违规人次
            List<Long> growIds = recDefaultService.listObjs(Wrappers.<RecDefault>lambdaQuery()
                    .select(RecDefault::getGrowId)
                    .in(RecDefault::getGrowId, growthItemMap.keySet())
                    .between(RecDefault::getCreateTime, startTime, endTime), id -> Long.valueOf(String.valueOf(id)));
            if (CollUtil.isNotEmpty(growIds)) {
                // 统计排序
                Map<Long, Long> growIdFfrequencyMap = growIds.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
                growIds.clear();
                Map<Long, Long> sortedGrowIdFrequencyMap = growIdFfrequencyMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                growIdFfrequencyMap.clear();
                int index = 1;
                for (Map.Entry<Long, Long> entry : sortedGrowIdFrequencyMap.entrySet()) {
                    // 只取前三条
                    if (index > 3) continue;
                    StudentGrowthMonitorVO studentGrowthMonitorVO = new StudentGrowthMonitorVO();
                    studentGrowthMonitorVO.setGrowItemName(growthItemMap.get(entry.getKey()));
                    studentGrowthMonitorVO.setPersonNum(entry.getValue());
                    resultList.add(studentGrowthMonitorVO);
                    index++;
                }
                growthItemMap.clear();
                sortedGrowIdFrequencyMap.clear();
            }
        }
        return resultList;
    }

    @Override
    public List<YearAtSchoolNumVO> countNear3YearsAtSchoolNum() {
        return yearInfoMapper.countNear3YearsAtSchoolNum();
    }

    @Override
    public List<AllKindsOfCompetitionAwardNumVO> countAllKindsOfCompetitionAwardNum() {
        List<AllKindsOfCompetitionAwardNumVO> resultList = new ArrayList<>();
        Year currYear = yearMapper.findCurrYear();

        for (Map.Entry<String, String> entry : COMPETITION_AWARD_LEVEL_OF_CONFIG_KEY_MAP.entrySet()) {
            AllKindsOfCompetitionAwardNumVO allKindsOfCompetitionAwardNumVO = new AllKindsOfCompetitionAwardNumVO();
            allKindsOfCompetitionAwardNumVO.setAwardType(entry.getKey());
            allKindsOfCompetitionAwardNumVO.setAwardNum(0L);
            if (currYear != null) {
                Config config = configService.selectByPrimaryKey(entry.getValue());
                Optional.ofNullable(config.getConfigValue()).ifPresent(configValue -> {
                    List<String> growthCodes = Arrays.asList(configValue.split(","));
                    if (CollUtil.isNotEmpty(growthCodes)) {
                        List<Long> growIds = growthItemService.listObjs(Wrappers.<GrowthItem>lambdaQuery()
                                        .select(GrowthItem::getId)
                                        .in(GrowthItem::getCode, growthCodes),
                                id -> Long.valueOf(String.valueOf(id)));
                        if (CollUtil.isNotEmpty(growIds)) {
                            long count = recDefaultService.count(Wrappers.<RecDefault>lambdaQuery()
                                    .between(RecDefault::getCreateTime, currYear.getYearStart(), currYear.getYearEnd())
                                    .in(RecDefault::getGrowId, growIds));
                            allKindsOfCompetitionAwardNumVO.setAwardNum(count);
                        }
                    }
                });
            }
            resultList.add(allKindsOfCompetitionAwardNumVO);
        }
        return resultList;
    }

    @Override
    public Long countScholarshipNum() {
        long count = 0;
        Year currYear = yearMapper.findCurrYear();
        if (currYear != null) {
            Config config = configService.selectByPrimaryKey(CommonConstants.ConfigKey.SCHOLARSHIP_GROWTH_CODE.getKey());
            String configValue = config.getConfigValue();
            if (configValue != null) {
                List<String> growthCodes = Arrays.asList(configValue.split(","));
                if (CollUtil.isNotEmpty(growthCodes)) {
                    List<Long> growIds = growthItemService.listObjs(Wrappers.<GrowthItem>lambdaQuery()
                                    .select(GrowthItem::getId)
                                    .in(GrowthItem::getCode, growthCodes),
                            id -> Long.valueOf(String.valueOf(id)));
                    if (CollUtil.isNotEmpty(growIds)) {
                        count = recDefaultService.count(Wrappers.<RecDefault>lambdaQuery()
                                .between(RecDefault::getCreateTime, currYear.getYearStart(), currYear.getYearEnd())
                                .in(RecDefault::getGrowId, growIds));
                    }
                }
            }
        }
        return count;
    }

    @Override
    public Long countHoldAnActivityNum() {
        long count = 0;
        Year currYear = yearMapper.findCurrYear();
        if (currYear != null) {
            Config config = configService.selectByPrimaryKey(CommonConstants.ConfigKey.HOLD_AN_ACTIVITY_GROWTH_CODE.getKey());
            String configValue = config.getConfigValue();
            if (configValue != null) {
                List<String> growthCodes = Arrays.asList(configValue.split(","));
                if (CollUtil.isNotEmpty(growthCodes)) {
                    List<Long> growIds = growthItemService.listObjs(Wrappers.<GrowthItem>lambdaQuery()
                                    .select(GrowthItem::getId)
                                    .in(GrowthItem::getCode, growthCodes),
                            id -> Long.valueOf(String.valueOf(id)));
                    if (CollUtil.isNotEmpty(growIds)) {
                        count = recDefaultService.count(Wrappers.<RecDefault>lambdaQuery()
                                .between(RecDefault::getCreateTime, currYear.getYearStart(), currYear.getYearEnd())
                                .in(RecDefault::getGrowId, growIds));
                    }
                }
            }
        }
        return count;
    }

    //TODO 加缓存
    @Override
    public List<GrowthScoreCountVO> countGrowthScoreForLastMonth() {
        List<GrowthScoreCountVO> resultList = new ArrayList<>();

        List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getName)
                .eq(Growth::getPid, 0));

        if (CollUtil.isNotEmpty(growths)) {
            Map<Long, String> growthByIdMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
            growths.clear();

            Date date = new Date();
            DateTime offset1Month = DateUtil.offset(date, DateField.MONTH, -1);
            DateTime offset2Month = DateUtil.offset(offset1Month, DateField.MONTH, -1);
            DateTime offset1MonthBegin = DateUtil.beginOfMonth(offset1Month);
            DateTime offset1MonthEnd = DateUtil.endOfMonth(offset1Month);
            DateTime offset2MonthBegin = DateUtil.beginOfMonth(offset2Month);
            DateTime offset2MonthEnd = DateUtil.endOfMonth(offset2Month);

            for (Map.Entry<Long, String> entry : growthByIdMap.entrySet()) {
                GrowthScoreCountVO growthScoreCountVO = new GrowthScoreCountVO();
                growthScoreCountVO.setGrowthName(entry.getValue());
                BigDecimal totalScore = BigDecimal.ZERO;
                List<Long> growthItems = growthItemService.listObjs(Wrappers.<GrowthItem>lambdaQuery()
                                .select(GrowthItem::getId)
                                .eq(GrowthItem::getFirstLevelId, entry.getKey()),
                        id -> Long.valueOf(String.valueOf(id)));

                if (CollUtil.isNotEmpty(growthItems)) {
                    growthScoreCountVO.setGrowthItemCount(growthItems.size());
                    // 查询上月总积分
                    List<BigDecimal> offset1MonthScores = recScoreService.listObjs(Wrappers.<RecScore>lambdaQuery()
                                    .select(RecScore::getScore)
                                    .in(RecScore::getGrowId, growthItems)
                                    .between(RecScore::getCreateTime, offset1MonthBegin, offset1MonthEnd),
                            score -> (BigDecimal) score);
                    if (CollUtil.isNotEmpty(offset1MonthScores)) {
                        totalScore = offset1MonthScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    // 查询上上月总积分
                    List<BigDecimal> offset2MonthScores = recScoreService.listObjs(Wrappers.<RecScore>lambdaQuery()
                                    .select(RecScore::getScore)
                                    .in(RecScore::getGrowId, growthItems)
                                    .between(RecScore::getCreateTime, offset2MonthBegin, offset2MonthEnd),
                            score -> (BigDecimal) score);
                    if (CollUtil.isNotEmpty(offset2MonthScores)) {
                        BigDecimal lastTotalScore = offset2MonthScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                        if (totalScore.compareTo(lastTotalScore) > 0) {
                            growthScoreCountVO.setScoreChangeType(ChangeTypeEnum.UP.getValue());
                            growthScoreCountVO.setChangeValue(totalScore.subtract(lastTotalScore));
                        } else if (totalScore.compareTo(lastTotalScore) < 0) {
                            growthScoreCountVO.setScoreChangeType(ChangeTypeEnum.DOWN.getValue());
                            growthScoreCountVO.setChangeValue(lastTotalScore.subtract(totalScore));
                        } else {
                            growthScoreCountVO.setScoreChangeType(ChangeTypeEnum.SAME.getValue());
                        }
                    }
                }
                growthScoreCountVO.setTotalScore(totalScore);
                resultList.add(growthScoreCountVO);
            }
        }
        return resultList;
    }
}
