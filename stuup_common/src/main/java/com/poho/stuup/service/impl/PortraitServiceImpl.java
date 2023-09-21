package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.L1GrowthScoreDTO;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.PortraitService;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.SemesterService;
import com.poho.stuup.util.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author BUNGA
 * @description: 学生画像 实现类
 * @date 2023/8/7 9:39
 */
@Slf4j
@Service
public class PortraitServiceImpl implements PortraitService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private StuScoreMapper stuScoreMapper;

    @Resource
    private YearMapper yearMapper;

    @Resource
    private GrowthMapper growthMapper;

    @Resource
    private GrowthItemMapper growthItemMapper;

    @Resource
    private RecAddScoreMapper recAddScoreMapper;

    @Resource
    private RecDeductScoreMapper recDeductScoreMapper;

    @Resource
    private IConfigService configService;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private SemesterService semesterService;

    @Resource
    private RankSemesterMapper rankSemesterMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private RecSocietyMapper recSocietyMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private FileMapper fileMapper;

    @Override
    public PortraitBasicInfoVO getBasicInfo(Student student, User user) {
        PortraitBasicInfoVO portraitBasicInfoVO = new PortraitBasicInfoVO();
        portraitBasicInfoVO.setStudentName(student.getName());
        portraitBasicInfoVO.setStudentNo(student.getStudentNo());
        portraitBasicInfoVO.setPhone(student.getPhone());
        if (user.getAvatarId() != null) {
            try {
                File file = fileMapper.selectById(user.getAvatarId());
                String bucket = file.getBucket();
                String storageName = file.getStorageName();
                String url = MinioUtils.getPreSignedObjectUrl(bucket, storageName);
                portraitBasicInfoVO.setAvatar(url);
            } catch (Exception e) {
                log.error("获取用户头像url失败");
            }
        }
        Integer classId = student.getClassId();
        if (classId != null) {
            Class _class = classMapper.selectByPrimaryKey(classId);
            if (_class != null) {
                portraitBasicInfoVO.setClassName(_class.getName());
                Integer teacherId = _class.getTeacherId();
                if (teacherId != null) {
                    Teacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
                    if (teacher != null) {
                        portraitBasicInfoVO.setClassTeacher(teacher.getName());
                    }
                }
            }
        }
        Integer majorId = student.getMajorId();
        if (majorId != null) {
            Major major = majorMapper.selectByPrimaryKey(majorId);
            if (major != null) {
                portraitBasicInfoVO.setMajorName(major.getMajorName());
            }
        }
        Integer studentId = student.getId();
        // 获取总分
        StuScore stuScore = stuScoreMapper.selectOne(Wrappers.<StuScore>lambdaQuery()
                .select(StuScore::getScore)
                .eq(StuScore::getStudentId, studentId));
        portraitBasicInfoVO.setTotalScore(stuScore == null ? BigDecimal.ZERO : stuScore.getScore());
        // 获取总扣分
        BigDecimal totalDeductScore = recDeductScoreMapper.fetchTotalScore(studentId, null, null);
        portraitBasicInfoVO.setTotalMinusScore(totalDeductScore);
        // 获取排名
        Integer ranking = recAddScoreService.getStudentNowRanking(Long.valueOf(studentId));
        portraitBasicInfoVO.setRanking(ranking);
        // 获取参加的社团
        List<RecSociety> recSocieties = recSocietyMapper.selectList(Wrappers.<RecSociety>lambdaQuery()
                .select(RecSociety::getName)
                .eq(RecSociety::getStudentId, studentId));
        List<String> communityMemberNames = recSocieties.stream().map(RecSociety::getName).collect(Collectors.toList());
        portraitBasicInfoVO.setAssociations(communityMemberNames);
        return portraitBasicInfoVO;
    }

    @Override
    public List<PortraitCapacityEvaluatorVO> getCapacityEvaluator(Long studentId) {
        List<PortraitCapacityEvaluatorVO> result = new ArrayList<>();
        Long yearId = yearMapper.getCurrentYearId();
        if (yearId == null) return result;
        Integer totalStudentNum = studentMapper.countAtSchoolNum();
        List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getName)
                .eq(Growth::getPid, 0));
        List<L1GrowthScoreDTO> studentL1GrowthScores = recAddScoreMapper.selectL1GrowthStudentScore(yearId, studentId);
        Map<Long, BigDecimal> studentL1GrowthScoreMap = studentL1GrowthScores.stream().collect(Collectors.toMap(L1GrowthScoreDTO::getL1Id, L1GrowthScoreDTO::getScore));
        List<L1GrowthScoreDTO> totalL1GrowthScores = recAddScoreMapper.selectL1GrowthTotalScore(yearId);
        Map<Long, BigDecimal> totalL1GrowthScoresMap = totalL1GrowthScores.stream().collect(Collectors.toMap(L1GrowthScoreDTO::getL1Id, L1GrowthScoreDTO::getScore));

        for (Growth growth : growths) {
            PortraitCapacityEvaluatorVO portraitCapacityEvaluatorVO = new PortraitCapacityEvaluatorVO();
            BigDecimal highestScore = recAddScoreMapper.selectL1GrowthHighestScore(yearId, growth.getId());
            BigDecimal studentScore = studentL1GrowthScoreMap.getOrDefault(growth.getId(), BigDecimal.ZERO);
            BigDecimal totalScore = totalL1GrowthScoresMap.getOrDefault(growth.getId(), BigDecimal.ZERO);
            BigDecimal avgScore = totalScore.divide(BigDecimal.valueOf(totalStudentNum), 2, RoundingMode.HALF_UP);
            portraitCapacityEvaluatorVO.setIndicatorName(growth.getName());
            portraitCapacityEvaluatorVO.setHighestScore(highestScore);
            portraitCapacityEvaluatorVO.setMyScore(studentScore);
            portraitCapacityEvaluatorVO.setAvgScore(avgScore);
            result.add(portraitCapacityEvaluatorVO);
        }

        return result;
    }

    @Override
    public List<PortraitAwardRecordVO> getAwardRecord(Long studentId) {
        List<PortraitAwardRecordVO> result = new ArrayList<>();
        Map<String, Integer> configKeyMap = new HashMap<>();
        configKeyMap.put(ConfigKeyEnum.NATIONAL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey(), RecLevelEnum.COUNTRY.getValue());
        configKeyMap.put(ConfigKeyEnum.CITY_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey(), RecLevelEnum.CITY.getValue());
        configKeyMap.put(ConfigKeyEnum.DISTRICT_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey(), RecLevelEnum.DISTRICT.getValue());
        configKeyMap.put(ConfigKeyEnum.SCHOOL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey(), RecLevelEnum.SCHOOL.getValue());
        Set<Map.Entry<String, Integer>> entries = configKeyMap.entrySet();
        List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getName));
        Map<Long, String> growthMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
        growths.clear();
        for (Map.Entry<String, Integer> entry : entries) {
            String configKey = entry.getKey();
            Integer awardType = entry.getValue();
            Config config = configService.selectByPrimaryKey(configKey);
            if (config != null) {
                String configValue = config.getConfigValue();
                List<String> growthItemCodes = Arrays.asList(configValue.split(","));
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getName, GrowthItem::getFirstLevelId, GrowthItem::getSecondLevelId, GrowthItem::getThreeLevelId)
                        .in(GrowthItem::getCode, growthItemCodes));
                Map<Long, GrowthItem> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getId, Function.identity()));
                List<Long> growthItemIds = growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList());
                growthItems.clear();
                List<RecAddScore> recAddScores = recAddScoreMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                        .select(RecAddScore::getGrowId, RecAddScore::getCreateTime)
                        .eq(RecAddScore::getStudentId, studentId)
                        .in(RecAddScore::getGrowId, growthItemIds)
                        .orderByDesc(RecAddScore::getCreateTime));
                growthItemIds.clear();
                for (RecAddScore recAddScore : recAddScores) {
                    PortraitAwardRecordVO portraitAwardRecordVO = new PortraitAwardRecordVO();
                    Long growId = recAddScore.getGrowId();
                    GrowthItem growthItem = growthItemMap.get(growId);
                    if (growthItem == null) continue;
                    String growthItemName = "";

                    Long threeLevelId = growthItem.getThreeLevelId();
                    if (threeLevelId != null) {
                        growthItemName = growthMap.get(threeLevelId);
                    } else {
                        Long secondLevelId = growthItem.getSecondLevelId();
                        if (secondLevelId != null) {
                            growthItemName = growthMap.get(secondLevelId);
                        } else {
                            Long firstLevelId = growthItem.getFirstLevelId();
                            if (firstLevelId != null) {
                                growthItemName = growthMap.get(firstLevelId);
                            }
                        }
                    }
                    growthItemName = growthItemName + "--" + growthItem.getName();
                    Date createTime = recAddScore.getCreateTime();
                    portraitAwardRecordVO.setAwardName(growthItemName);
                    portraitAwardRecordVO.setAwardType(awardType);
                    portraitAwardRecordVO.setAwardTime(createTime);
                    result.add(portraitAwardRecordVO);
                }
            }
        }
        if (CollUtil.isEmpty(result)) return result;
        return result.stream().sorted(Comparator.comparing(PortraitAwardRecordVO::getAwardTime, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public List<PortraitActivityRecordVO> getActivityRecord(Long studentId) {
        List<PortraitActivityRecordVO> result = new ArrayList<>();
        Config config = configService.selectByPrimaryKey(ConfigKeyEnum.HOLD_AN_ACTIVITY_GROWTH_CODE.getKey());
        if (config != null) {
            List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                    .select(Growth::getId, Growth::getName));
            Map<Long, String> growthMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
            growths.clear();
            String configValue = config.getConfigValue();
            List<String> growthItemCodes = Arrays.asList(configValue.split(","));
            List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                    .select(GrowthItem::getId, GrowthItem::getName, GrowthItem::getFirstLevelId, GrowthItem::getSecondLevelId, GrowthItem::getThreeLevelId)
                    .in(GrowthItem::getCode, growthItemCodes));
            Map<Long, GrowthItem> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getId, Function.identity()));
            List<Long> growthItemIds = growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList());
            growthItems.clear();
            List<RecAddScore> recAddScores = recAddScoreMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                    .select(RecAddScore::getGrowId, RecAddScore::getCreateTime)
                    .eq(RecAddScore::getStudentId, studentId)
                    .in(RecAddScore::getGrowId, growthItemIds)
                    .orderByDesc(RecAddScore::getCreateTime));
            growthItemIds.clear();
            for (RecAddScore recAddScore : recAddScores) {
                PortraitActivityRecordVO portraitActivityRecordVO = new PortraitActivityRecordVO();
                Long growId = recAddScore.getGrowId();
                GrowthItem growthItem = growthItemMap.get(growId);
                if (growthItem == null) continue;
                String growthItemName = "";
                Long threeLevelId = growthItem.getThreeLevelId();
                if (threeLevelId != null) {
                    growthItemName = growthMap.get(threeLevelId);
                } else {
                    Long secondLevelId = growthItem.getSecondLevelId();
                    if (secondLevelId != null) {
                        growthItemName = growthMap.get(secondLevelId);
                    } else {
                        Long firstLevelId = growthItem.getFirstLevelId();
                        if (firstLevelId != null) {
                            growthItemName = growthMap.get(firstLevelId);
                        }
                    }
                }
                growthItemName = growthItemName + "--" + growthItem.getName();
                Date createTime = recAddScore.getCreateTime();
                portraitActivityRecordVO.setActivityName(growthItemName);
                portraitActivityRecordVO.setActivityTime(createTime);
                result.add(portraitActivityRecordVO);
            }
        }
        if (CollUtil.isEmpty(result)) return result;
        return result.stream().sorted(Comparator.comparing(PortraitActivityRecordVO::getActivityTime, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    @Override
    public ResponseModel<List<PortraitRankingCurveVO>> getRankingCurve(Student student) {
        List<Semester> semesters = semesterService.getStudentSemester(student.getId());
        if (CollUtil.isEmpty(semesters)) return ResponseModel.failed("未查询到学期信息");
        ArrayList<PortraitRankingCurveVO> result = new ArrayList<>();
        Integer studentId = student.getId();
        for (Semester semester : semesters) {
            Long semesterId = semester.getId();
            String semesterName = semester.getName();
            PortraitRankingCurveVO portraitRankingCurveVO = new PortraitRankingCurveVO();
            portraitRankingCurveVO.setSemesterName(semesterName);
            RankSemester rankSemester = rankSemesterMapper.selectOne(Wrappers.<RankSemester>lambdaQuery()
                    .eq(RankSemester::getSemesterId, semesterId)
                    .eq(RankSemester::getStudentId, studentId));
            if (rankSemester != null) {
                BigDecimal score = rankSemester.getScore();
                Integer ranking = rankSemester.getRanking();
                portraitRankingCurveVO.setScore(score);
                portraitRankingCurveVO.setRank(ranking);
            }
            result.add(portraitRankingCurveVO);
        }
        return ResponseModel.ok(result);
    }

    @Override
    public ResponseModel<List<PortraitGrowthAnalysisVO>> getGrowthAnalysis(Student student) {
        List<Semester> studentSemester = semesterService.getStudentSemester(student.getId());
        if (CollUtil.isEmpty(studentSemester)) return ResponseModel.failed("未查询到学期信息");
        List<PortraitGrowthAnalysisVO> result = new ArrayList<>();
        Integer studentId = student.getId();
        for (Semester semester : studentSemester) {
            Long semesterId = semester.getId();
            String semesterName = semester.getName();
            PortraitGrowthAnalysisVO portraitGrowthAnalysisVO = new PortraitGrowthAnalysisVO();
            portraitGrowthAnalysisVO.setSemesterName(semesterName);
            // 查询平均分
            List<RankSemester> rankSemesters = rankSemesterMapper.selectList(Wrappers.<RankSemester>lambdaQuery()
                    .select(RankSemester::getScore)
                    .eq(RankSemester::getSemesterId, semesterId));
            if (CollUtil.isNotEmpty(rankSemesters)) {
                BigDecimal studentScore = BigDecimal.ZERO, avgScore, avgScoreGap;
                BigDecimal totalScore = rankSemesters.stream().map(RankSemester::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
                avgScore = totalScore.divide(BigDecimal.valueOf(rankSemesters.size()), 2, RoundingMode.HALF_UP);
                // 查询本人该学期积分
                RankSemester studentRankSemester = rankSemesterMapper.selectOne(Wrappers.<RankSemester>lambdaQuery()
                        .select(RankSemester::getScore)
                        .eq(RankSemester::getSemesterId, semesterId)
                        .eq(RankSemester::getStudentId, studentId));
                if (studentRankSemester != null) {
                    studentScore = studentRankSemester.getScore();
                }
                avgScoreGap = studentScore.subtract(avgScore);
                portraitGrowthAnalysisVO.setAvgScoreGap(avgScoreGap);
            }
            result.add(portraitGrowthAnalysisVO);
        }
        return ResponseModel.ok(result);
    }


    @Override
    public PortraitGrowthDataVO getGrowthData(Integer studentId, Long semesterId) {
        PortraitGrowthDataVO portraitGrowthDataVO = new PortraitGrowthDataVO();
        // 查询成长值和排名
        RankSemester rankSemester = rankSemesterMapper.selectOne(Wrappers.<RankSemester>lambdaQuery()
                .select(RankSemester::getScore, RankSemester::getRanking)
                .eq(RankSemester::getStudentId, studentId)
                .eq(RankSemester::getSemesterId, semesterId));
        if (rankSemester != null) {
            BigDecimal score = rankSemester.getScore();
            Integer ranking = rankSemester.getRanking();
            portraitGrowthDataVO.setScore(score);
            portraitGrowthDataVO.setRank(ranking);
        }
        // 查询扣除分
        BigDecimal totalDeductScore = recDeductScoreMapper.fetchTotalScore(studentId, null, semesterId);
        portraitGrowthDataVO.setMinusScore(totalDeductScore);

        // 查询参加活动次数
        Config config1 = configService.selectByPrimaryKey(ConfigKeyEnum.HOLD_AN_ACTIVITY_GROWTH_CODE.getKey());
        if (config1 != null) {
            String configValue = config1.getConfigValue();
            List<String> codes = Arrays.asList(configValue.split(","));
            if (CollUtil.isNotEmpty(codes)) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId)
                        .in(GrowthItem::getCode, codes));
                List<Long> growthItemIds = growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList());
                Long activityCount = recAddScoreMapper.selectCount(Wrappers.<RecAddScore>lambdaQuery()
                        .eq(RecAddScore::getStudentId, studentId)
                        .eq(RecAddScore::getSemesterId, semesterId)
                        .in(RecAddScore::getId, growthItemIds));
                portraitGrowthDataVO.setActivityCount(activityCount);
            }
        }
        // 查询获奖次数
        List<String> configKeys = Arrays.asList(ConfigKeyEnum.NATIONAL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey(),
                ConfigKeyEnum.CITY_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey(),
                ConfigKeyEnum.DISTRICT_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey(),
                ConfigKeyEnum.SCHOOL_LEVEL_COMPETITION_AWARD_GROWTH_CODE.getKey());
        List<String> growthItemCodes = new ArrayList<>();
        int size = configKeys.size();
        for (int i = 0; i < size; i++) {
            String configKey = configKeys.get(i);
            Config config2 = configService.selectByPrimaryKey(configKey);
            if (config2 != null) {
                String configValue = config2.getConfigValue();
                List<String> codes = Arrays.asList(configValue.split(","));
                growthItemCodes.addAll(codes);
            }
        }
        if (CollUtil.isNotEmpty(growthItemCodes)) {
            List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                    .select(GrowthItem::getId)
                    .in(GrowthItem::getCode, growthItemCodes));
            List<Long> growthItemIds = growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList());
            Long awardCount = recAddScoreMapper.selectCount(Wrappers.<RecAddScore>lambdaQuery()
                    .eq(RecAddScore::getSemesterId, semesterId)
                    .eq(RecAddScore::getStudentId, studentId)
                    .in(RecAddScore::getGrowId, growthItemIds));
            portraitGrowthDataVO.setAwardCount(awardCount);
        }
        // 查询获得证书次数
        Config config3 = configService.selectByPrimaryKey(ConfigKeyEnum.GET_CERTIFICATE_GROWTH_CODE.getKey());
        if (config3 != null) {
            String configValue = config3.getConfigValue();
            List<String> codes = Arrays.asList(configValue.split(","));
            if (CollUtil.isNotEmpty(codes)) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId)
                        .in(GrowthItem::getCode, codes));
                List<Long> growthItemIds = growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList());
                Long certificateCount = recAddScoreMapper.selectCount(Wrappers.<RecAddScore>lambdaQuery()
                        .eq(RecAddScore::getStudentId, studentId)
                        .eq(RecAddScore::getSemesterId, semesterId)
                        .in(RecAddScore::getId, growthItemIds));
                portraitGrowthDataVO.setCertificateCount(certificateCount);
            }
        }
        return portraitGrowthDataVO;
    }

    @Override
    public List<PortraitGrowthComparisonVO> getGrowthComparison(Long studentId, Long semesterId) {
        List<PortraitGrowthComparisonVO> result = new ArrayList<>();
        // 查询所有一级项目
        List<Growth> firstLevelList = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getName)
                .eq(Growth::getPid, 0));
        Map<Long, String> firstLevelMap = firstLevelList.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
        firstLevelList.clear();
        // 查询所有成长项
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getFirstLevelId)
                .isNotNull(GrowthItem::getFirstLevelId));
        Map<Long, Long> getFirstLevelByIdMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getId, GrowthItem::getFirstLevelId));
        growthItems.clear();
        // 查询当前学期所有的得分记录
        List<RecAddScore> allRecAddScore = recAddScoreMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getGrowId, RecAddScore::getScore, RecAddScore::getStudentId)
                .eq(RecAddScore::getSemesterId, semesterId));
        // 过滤出当前学生的记录
        List<RecAddScore> studentRecAddScore = allRecAddScore.stream().filter(recAddScore -> Objects.equals(recAddScore.getStudentId(), studentId)).collect(Collectors.toList());
        // 按一级项目分组求和
        Map<Long, BigDecimal> allScoreSumByGrowId = allRecAddScore.stream()
                .filter(recAddScore -> getFirstLevelByIdMap.containsKey(recAddScore.getGrowId()))   // 过滤掉为空的结果
                .collect(Collectors.groupingBy(recAddScore -> getFirstLevelByIdMap.get(recAddScore.getGrowId()),
                        Collectors.mapping(RecAddScore::getScore, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        Map<Long, BigDecimal> newAllScoreSumByGrowId = new HashMap<>();
        // 查询所有学生人数
        Integer countStudent = studentMapper.countAtSchoolNum();
        allScoreSumByGrowId.forEach((growthItemId, sumScore) -> {
            BigDecimal avgScore = sumScore.divide(new BigDecimal(countStudent), 2, RoundingMode.HALF_UP);
            newAllScoreSumByGrowId.put(growthItemId, avgScore);
        });
        // 按项目分组求和
        Map<Long, BigDecimal> studentScoreSumByGrowId = studentRecAddScore.stream()
                .filter(recAddScore -> getFirstLevelByIdMap.containsKey(recAddScore.getGrowId()))   // 过滤掉为空的结果
                .collect(Collectors.groupingBy(recAddScore -> getFirstLevelByIdMap.get(recAddScore.getGrowId()),
                        Collectors.mapping(RecAddScore::getScore, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        // 遍历所有项目
        for (Map.Entry<Long, String> entry : firstLevelMap.entrySet()) {
            Long growthId = entry.getKey();
            String growthName = entry.getValue();
            PortraitGrowthComparisonVO portraitGrowthComparisonVO = new PortraitGrowthComparisonVO();
            portraitGrowthComparisonVO.setGrowthName(growthName);
            BigDecimal avgScore = newAllScoreSumByGrowId.getOrDefault(growthId, BigDecimal.ZERO);
            portraitGrowthComparisonVO.setAvgScore(avgScore);
            BigDecimal score = studentScoreSumByGrowId.getOrDefault(growthId, BigDecimal.ZERO);
            portraitGrowthComparisonVO.setMyScore(score);
            result.add(portraitGrowthComparisonVO);
        }
        return result;
    }

    @Override
    public ResponseModel<List<PortraitStudyGradeVO>> getStudyGrade(Student student) {
        Integer gradeId = student.getGradeId();
        Grade grade = gradeMapper.selectByPrimaryKey(gradeId);
        if (gradeId == null) return ResponseModel.failed("未查询到您的年级信息");
        String year = grade.getYear();
        Config config = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_START_TIME.getKey());
        if (config == null) return ResponseModel.failed("未查询到上学期开始时间的配置");
        Date startTime = DateUtil.parseDate(StrUtil.format("{}-{}", year, config.getConfigValue()));
        Semester currSemester = semesterMapper.selectOne(Wrappers.<Semester>lambdaQuery()
                .select(Semester::getStartTime)
                .eq(Semester::getIsCurrent, WhetherEnum.YES.getValue()));
        if (currSemester == null) return ResponseModel.failed("不在学期设置时间段内");
        Date endTime = currSemester.getStartTime();
        List<Semester> semesters = semesterMapper.selectList(Wrappers.<Semester>lambdaQuery()
                .select(Semester::getId, Semester::getName, Semester::getIsCurrent)
                .between(Semester::getStartTime, startTime, endTime)
                .orderByAsc(Semester::getStartTime));
        if (CollUtil.isEmpty(semesters)) return ResponseModel.failed("未查询到学期信息");
        List<PortraitStudyGradeVO> result = new ArrayList<>();
        Integer studentId = student.getId();
        for (Semester semester : semesters) {
            Long semesterId = semester.getId();
            String semesterName = semester.getName();
            List<Course> courses = courseMapper.selectList(Wrappers.<Course>lambdaQuery()
                    .select(Course::getCourseScore)
                    .eq(Course::getStudentId, studentId)
                    .eq(Course::getSemesterId, semesterId));
            Float totalScore = courses.stream().map(Course::getCourseScore).reduce(0f, Float::sum);
            PortraitStudyGradeVO portraitStudyGradeVO = new PortraitStudyGradeVO();
            portraitStudyGradeVO.setSemesterName(semesterName);
            portraitStudyGradeVO.setScore(totalScore);
            result.add(portraitStudyGradeVO);
        }
        return ResponseModel.ok(result);
    }

    @Override
    public ResponseModel<List<PortraitStudyCourseVO>> getStudyCourse(Long studentId, Long semesterId) {
        List<Course> courses = courseMapper.selectList(Wrappers.<Course>lambdaQuery()
                .select(Course::getCourseName, Course::getCourseScore)
                .eq(Course::getSemesterId, semesterId)
                .eq(Course::getStudentId, studentId));
        List<PortraitStudyCourseVO> result = new ArrayList<>();
        for (Course course : courses) {
            String courseName = course.getCourseName();
            Float courseScore = course.getCourseScore();
            PortraitStudyCourseVO portraitStudyCourseVO = new PortraitStudyCourseVO();
            portraitStudyCourseVO.setCourseName(courseName);
            portraitStudyCourseVO.setScore(courseScore);
            result.add(portraitStudyCourseVO);
        }
        return ResponseModel.ok(result);
    }
}
