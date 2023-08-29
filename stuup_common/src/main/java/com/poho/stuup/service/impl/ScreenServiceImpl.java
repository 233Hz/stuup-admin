package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.ChangeTypeEnum;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.*;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScreenServiceImpl implements ScreenService {

    private final static Map<String, String> COMPETITION_AWARD_LEVEL_OF_CONFIG_KEY_MAP = new HashMap<String, String>() {
        {
            put("国家级", ConfigKeyEnum.NATIONAL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
            put("市级", ConfigKeyEnum.CITY_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
            put("区级", ConfigKeyEnum.DISTRICT_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
            put("校级", ConfigKeyEnum.SCHOOL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
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
    private RecAddScoreService recAddScoreService;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ClassMapper classMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private GrowthItemMapper growthItemMapper;
    @Resource
    private LoginLogMapper loginLogMapper;
    @Resource
    private RecLogService recLogService;

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
                            long count = recAddScoreService.count(Wrappers.<RecAddScore>lambdaQuery()
                                    .between(RecAddScore::getCreateTime, currYear.getYearStart(), currYear.getYearEnd())
                                    .in(RecAddScore::getGrowId, growIds));
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
            Config config = configService.selectByPrimaryKey(ConfigKeyEnum.SCHOLARSHIP_GROWTH_CODE.getKey());
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
            Config config = configService.selectByPrimaryKey(ConfigKeyEnum.HOLD_AN_ACTIVITY_GROWTH_CODE.getKey());
            String configValue = config.getConfigValue();
            if (configValue != null) {
                List<String> growthCodes = Arrays.asList(configValue.split(","));
                if (CollUtil.isNotEmpty(growthCodes)) {
                    List<Long> growIds = growthItemService.listObjs(Wrappers.<GrowthItem>lambdaQuery()
                                    .select(GrowthItem::getId)
                                    .in(GrowthItem::getCode, growthCodes),
                            id -> Long.valueOf(String.valueOf(id)));
                    if (CollUtil.isNotEmpty(growIds)) {
                        count = recLogService.count(Wrappers.<RecLog>lambdaQuery()
                                .between(RecLog::getCreateTime, currYear.getYearStart(), currYear.getYearEnd())
                                .in(RecLog::getGrowId, growIds));
                    }
                }
            }
        }
        return count;
    }

    //TODO 加缓存
    @Override
    public List<GrowthScoreCountVO> countGrowthScoreCompare() {
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
                    List<BigDecimal> offset1MonthScores = recAddScoreService.listObjs(Wrappers.<RecAddScore>lambdaQuery()
                                    .select(RecAddScore::getScore)
                                    .in(RecAddScore::getGrowId, growthItems)
                                    .between(RecAddScore::getCreateTime, offset1MonthBegin, offset1MonthEnd),
                            score -> (BigDecimal) score);
                    if (CollUtil.isNotEmpty(offset1MonthScores)) {
                        totalScore = offset1MonthScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    // 查询上上月总积分
                    List<BigDecimal> offset2MonthScores = recAddScoreService.listObjs(Wrappers.<RecAddScore>lambdaQuery()
                                    .select(RecAddScore::getScore)
                                    .in(RecAddScore::getGrowId, growthItems)
                                    .between(RecAddScore::getCreateTime, offset2MonthBegin, offset2MonthEnd),
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

    @Override
    public List<MajorPopulationsVO> countMajorPopulations() {
        return studentMapper.countMajorPopulations();
    }

    @Override
    public ScreenImportantDataVO getImportantData() {
        ScreenImportantDataVO screenImportantDataVO = new ScreenImportantDataVO();
        List<Student> students = studentMapper.getAllStudent();
        if (CollUtil.isNotEmpty(students)) {
            // 统计在校生人数
            screenImportantDataVO.setAtSchoolNum(students.size());
            int boyNum = 0, girlNum = 0;
            int size = students.size();
            for (int i = 0; i < size; i++) {
                Student student = students.get(i);
                Integer sex = student.getSex();
                if (sex == 1) boyNum++;
                if (sex == 2) girlNum++;
            }
            // 统计男女比例
            int gcd = Utils.calculatorGCD(boyNum, girlNum);
            String sexRatio = StrUtil.format("{}:{}", boyNum / gcd, girlNum / gcd);
            screenImportantDataVO.setSexRatio(sexRatio);
        }

        // 统计班级总数
        int classNum = classMapper.countClassTotal();
        screenImportantDataVO.setClassNum(classNum);

        // 统计专业总数
        int majorNum = majorMapper.countMajorTotal();
        screenImportantDataVO.setMajorNum(majorNum);

        // 统计成长项目总数
        Long growthItemNum = growthItemMapper.selectCount(Wrappers.lambdaQuery());
        screenImportantDataVO.setGrowthNum(growthItemNum);

        // 统计举办活动次数
        Long activityNum = countHoldAnActivityNum();
        screenImportantDataVO.setActivityNum(activityNum);

        // 统计获得奖学金人数
        Long scholarshipNum = countScholarshipNum();
        screenImportantDataVO.setScholarshipNum(scholarshipNum);

        return screenImportantDataVO;
    }

    @Override
    public List<DailyVisitsVO> countDailyVisits() {
        return loginLogMapper.countDailyVisits();
    }
}
