package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.CompareEnum;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.*;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
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
    private RecDefaultMapper recDefaultMapper;
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
    @Resource
    private AudGrowMapper audGrowMapper;


    @Override
    public List<StudentGrowthMonitorVO> studentGrowthMonitor() {
        // 查询扣分项
        List<GrowthItem> growthItems = growthItemService.list(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getName, GrowthItem::getId)
                .eq(GrowthItem::getCalculateType, CalculateTypeEnum.MINUS.getValue()));
        if (CollUtil.isNotEmpty(growthItems)) {
            Map<Long, String> growthItemIdForNameMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getId, GrowthItem::getName));
            growthItems.clear();
            // 获取本月起止时间
            Date date = new Date();
            DateTime startTime = DateUtil.beginOfMonth(date);
            DateTime endTime = DateUtil.endOfMonth(date);
            // 查询时间段内的违规人次
            List<StudentGrowthMonitorVO> result = recDefaultMapper.countViolationsTop3(growthItemIdForNameMap.keySet(), startTime, endTime);
            result
                    .forEach(item -> {
                        Long growthItemId = item.getGrowthItemId();
                        String growthItemName = growthItemIdForNameMap.get(growthItemId);
                        item.setGrowItemName(growthItemName);
                    });
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<YearAtSchoolNumVO> countNear3YearsAtSchoolNum() {
        List<YearAtSchoolNumVO> result = yearInfoMapper.countNear3YearsAtSchoolNum();
        Collections.reverse(result);
        return result;
    }

    @Override
    public List<AllKindsOfCompetitionAwardNumVO> countAllKindsOfCompetitionAwardNum() {
        List<AllKindsOfCompetitionAwardNumVO> resultList = new ArrayList<>();
        Year currYear = yearMapper.getCurrentYear();

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
        Year currYear = yearMapper.getCurrentYear();
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
        Year currYear = yearMapper.getCurrentYear();
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
                            growthScoreCountVO.setScoreChangeType(CompareEnum.UP.getValue());
                            growthScoreCountVO.setChangeValue(totalScore.subtract(lastTotalScore));
                        } else if (totalScore.compareTo(lastTotalScore) < 0) {
                            growthScoreCountVO.setScoreChangeType(CompareEnum.DOWN.getValue());
                            growthScoreCountVO.setChangeValue(lastTotalScore.subtract(totalScore));
                        } else {
                            growthScoreCountVO.setScoreChangeType(CompareEnum.SAME.getValue());
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
    public ScreenImportantDataVO getImportantData(StopWatch stopWatch) {


        stopWatch.start("男女统计");
        ScreenImportantDataVO screenImportantDataVO = new ScreenImportantDataVO();
        // 统计在校生人数
        Integer atSchoolNum = studentMapper.countAtSchoolNum();
        if (atSchoolNum != null) screenImportantDataVO.setAtSchoolNum(atSchoolNum);
        // 统计男女比例
        Integer boyNum = studentMapper.countSexNum(1);
        Integer girlNum = studentMapper.countSexNum(2);
        if (boyNum != null && girlNum != null) {
            // 统计男女比例
            int gcd = Utils.calculatorGCD(boyNum, girlNum);
            String sexRatio = StrUtil.format("{}:{}", boyNum / gcd, girlNum / gcd);
            screenImportantDataVO.setSexRatio(sexRatio);
        }
        stopWatch.stop();

        // 统计班级总数
        stopWatch.start("班级总数统计");
        int classNum = classMapper.countClassTotal();
        screenImportantDataVO.setClassNum(classNum);
        stopWatch.stop();

        // 统计专业总数
        stopWatch.start("专业总数统计");
        int majorNum = majorMapper.countMajorTotal();
        screenImportantDataVO.setMajorNum(majorNum);
        stopWatch.stop();

        // 统计成长项目总数
        stopWatch.start("成长项目总数统计");
        Long growthItemNum = growthItemMapper.selectCount(Wrappers.lambdaQuery());
        screenImportantDataVO.setGrowthNum(growthItemNum);
        stopWatch.stop();

        // 统计举办活动次数
        stopWatch.start("统计举办活动次数");
        Long activityNum = countHoldAnActivityNum();
        screenImportantDataVO.setActivityNum(activityNum);
        stopWatch.stop();

        // 统计获得奖学金人数
        stopWatch.start("获得奖学金人数统计");
        Long scholarshipNum = countScholarshipNum();
        screenImportantDataVO.setScholarshipNum(scholarshipNum);
        stopWatch.stop();

        Year year = yearMapper.getCurrentYear();
        if (year != null) {
            Date start = year.getYearStart();
            Date end = year.getYearEnd();
            // 统计学生申请项目数
            stopWatch.start("学生申请项目数统计");
            int applyCount = audGrowMapper.countApplyInRange(start, end);
            screenImportantDataVO.setApplyCount(applyCount);
            stopWatch.stop();
            // 统计班主任审核项目数
            stopWatch.start("统计班主任审核项目数");
            int auditApply = audGrowMapper.countAuditInRange(start, end);
            screenImportantDataVO.setAuditCount(auditApply);
            stopWatch.stop();
        }

        return screenImportantDataVO;
    }

    @Override
    public List<DailyVisitsVO> countDailyVisits() {
        return loginLogMapper.countDailyVisits();
    }

    @Override
    public List<ReviewOfEachClassVO> countReviewOfEachClass() {
        Year year = yearMapper.getCurrentYear();
        if (year == null) return Collections.emptyList();
        Date startTime = year.getYearStart();
        Date endTime = year.getYearEnd();
        List<ReviewOfEachClassVO> result = audGrowMapper.countReviewOfEachClass(startTime, endTime);
        if (CollUtil.isNotEmpty(result)) {
            Map<Long, ReviewOfEachClassVO> map = result.stream().collect(Collectors.toMap(ReviewOfEachClassVO::getClassTeacherId, Function.identity()));
            result.clear();
            List<Class> classList = classMapper.getAllClassNameAndTeacherId();
            for (Class _class : classList) {
                Optional.ofNullable(_class.getTeacherId())
                        .map(classId -> {
                            ReviewOfEachClassVO reviewOfEachClass = map.get(classId.longValue());
                            if (reviewOfEachClass == null) {
                                reviewOfEachClass = new ReviewOfEachClassVO();
                            }
                            reviewOfEachClass.setClassName(_class.getName());
                            return reviewOfEachClass;
                        })
                        .ifPresent(result::add);
            }
        }
        return result;
    }

    @Override
    public Integer countVisitsThisMonth() {
        return loginLogMapper.countVisitsThisMonth();
    }
}
