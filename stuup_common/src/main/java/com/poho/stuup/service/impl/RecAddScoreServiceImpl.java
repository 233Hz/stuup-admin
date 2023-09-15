package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.StudentRecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.model.vo.UnCollectScore;
import com.poho.stuup.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 积分记录表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Slf4j
@Service
public class RecAddScoreServiceImpl extends ServiceImpl<RecAddScoreMapper, RecAddScore> implements RecAddScoreService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private StuScoreService stuScoreService;

    @Resource
    private RecDefaultService recDefaultService;

    @Resource
    private GrowthItemService growthItemService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private StuScoreLogService stuScoreLogService;

    @Resource
    private StuGrowthMapper stuGrowthMapper;

    @Resource
    private StuGrowthService stuGrowthService;

    @Resource
    private RecAddScoreMapper recAddScoreMapper;

    @Resource
    private RecDeductScoreMapper recDeductScoreMapper;

    @Resource
    private StuScoreLogMapper stuScoreLogMapper;

    @Resource
    private GrowthMapper growthMapper;

    @Resource
    private GradeMapper gradeMapper;


    @Override
    public IPage<RecScoreVO> pageRecAddScore(Page<RecScoreVO> page, RecScoreDTO query) {
        IPage<RecScoreVO> pageResult = baseMapper.pageRecAddScore(page, query);
        List<RecScoreVO> pageRecords = pageResult.getRecords();
        if (pageRecords != null && !pageRecords.isEmpty()) {
            // 一二三级项目
            List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                    .select(Growth::getId, Growth::getName));
            Map<Long, String> growthIdForNameMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
            growths.clear();
            Set<Long> yearIdSet = new HashSet<>();
            Set<Long> semesterIdSet = new HashSet<>();
            Set<Integer> gradeIdSet = new HashSet<>();
            for (RecScoreVO record : pageRecords) {
                Optional.ofNullable(record.getYearId())
                        .ifPresent(yearIdSet::add);
                Optional.ofNullable(record.getSemesterId())
                        .ifPresent(semesterIdSet::add);
                Optional.ofNullable(record.getGradeId())
                        .ifPresent(gradeIdSet::add);
                Optional.ofNullable(record.getL1Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(record::setL1);
                Optional.ofNullable(record.getL2Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(record::setL2);
                Optional.ofNullable(record.getL3Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(record::setL3);
            }
            growthIdForNameMap.clear();
            // 年份
            Map<Long, String> yearIdForNameMap = new HashMap<>();
            Map<Long, String> semesterIdForNameMap = new HashMap<>();
            Map<Integer, String> gradeIdForNameMap = new HashMap<>();
            if (!yearIdSet.isEmpty()) {
                List<Year> yearList = yearMapper.getYearNameByIds(new ArrayList<>(yearIdSet));
                yearIdForNameMap = yearList.stream().collect(Collectors.toMap(Year::getOid, Year::getYearName));
                yearList.clear();
            }
            // 学期
            if (!semesterIdSet.isEmpty()) {
                List<Semester> semesterList = semesterMapper.getSemesterNameByIds(new ArrayList<>(semesterIdSet));
                semesterIdForNameMap = semesterList.stream().collect(Collectors.toMap(Semester::getId, Semester::getName));
                semesterList.clear();
            }
            // 年级
            if (!gradeIdSet.isEmpty()) {
                List<Grade> gradeList = gradeMapper.getGradeNameByIds(new ArrayList<>(gradeIdSet));
                gradeIdForNameMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid, Grade::getGradeName));
                gradeList.clear();
            }
            for (RecScoreVO record : pageRecords) {
                Optional.ofNullable(record.getYearId())
                        .map(yearIdForNameMap::get)
                        .ifPresent(record::setYearName);
                Optional.ofNullable(record.getSemesterId())
                        .map(semesterIdForNameMap::get)
                        .ifPresent(record::setSemesterName);
                Optional.ofNullable(record.getGradeId())
                        .map(gradeIdForNameMap::get)
                        .ifPresent(record::setGradeName);
            }
            yearIdSet.clear();
            semesterIdSet.clear();
            gradeIdSet.clear();
        }
        return pageResult;
    }

    @Override
    public ResponseModel<IPage<StudentRecScoreVO>> pageStudentRecScore(Page<StudentRecScoreVO> page, Long userId, StudentRecScoreDTO query) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ResponseModel.failed("未查询到您的用户信息，请联系管理员");
        String loginName = user.getLoginName();
        Long studentId = studentMapper.getIdByStudentNo(loginName);
        if (studentId == null) return ResponseModel.failed("未查询到您的学生信息，请联系管理员");
        return ResponseModel.ok(baseMapper.pageStudentRecScore(page, studentId, query));
    }

    @Override
    public void batchCalculateScore(List<Long> studentIds, GrowthItem growthItem, Long yearId, Long semesterId, Date time) {
        if (CollUtil.isEmpty(studentIds)) return;
        Long growthItemId = growthItem.getId();
        Integer calculateType = growthItem.getCalculateType();
        Integer scorePeriod = growthItem.getScorePeriod();
        Integer collectLimit = growthItem.getCollectLimit();
        BigDecimal growthItemScore = growthItem.getScore();
        PeriodEnum periodEnum = PeriodEnum.getByValue(scorePeriod);

        if (periodEnum == null) {
            throw new RuntimeException("项目积分刷新周期设置错误，请联系管理员");
        }

        if (calculateType != CalculateTypeEnum.PLUS.getValue() && calculateType != CalculateTypeEnum.MINUS.getValue())
            throw new RuntimeException("该项目计算类型不存在，请联系管理员修改");

        boolean isPlusCalculateType = calculateType == CalculateTypeEnum.PLUS.getValue();
        boolean hasCollectLimit = collectLimit != null && collectLimit > 0;

        if (PeriodEnum.UNLIMITED.getValue() == scorePeriod) {
            if (hasCollectLimit) {
                // 获取学生的积分采集次数信息
                List<StuGrowth> stuGrowths = stuGrowthMapper.selectList(Wrappers.<StuGrowth>lambdaQuery()
                        .select(StuGrowth::getStudentId, StuGrowth::getCount)
                        .eq(StuGrowth::getGrowId, growthItemId));
                // 保存每个学生已使用的采集次数
                Map<Long, Integer> studentCollectLimitMap = stuGrowths.stream().collect(Collectors.toMap(StuGrowth::getStudentId, StuGrowth::getCount));
                if (isPlusCalculateType) {
                    for (Long studentId : studentIds) {
                        // 有次数限制  加分
                        Integer useCollectCount = studentCollectLimitMap.getOrDefault(studentId, 0);
                        StuScoreLog stuScoreLog = new StuScoreLog();
                        stuScoreLog.setYearId(yearId);
                        stuScoreLog.setSemesterId(semesterId);
                        stuScoreLog.setStudentId(studentId);
                        stuScoreLog.setGrowId(growthItemId);
                        stuScoreLog.setCreateTime(time);
                        stuScoreLog.setScore(growthItemScore);
                        if (useCollectCount < collectLimit) {
                            RecAddScore recAddScore = new RecAddScore();
                            recAddScore.setYearId(yearId);
                            recAddScore.setSemesterId(semesterId);
                            recAddScore.setStudentId(studentId);
                            recAddScore.setGrowId(growthItemId);
                            recAddScore.setCreateTime(time);
                            recAddScore.setScore(growthItemScore);
                            recAddScoreMapper.insert(recAddScore);
                            stuScoreService.updateTotalScore(studentId, growthItemScore);
                            studentCollectLimitMap.put(studentId, useCollectCount + 1);
                        } else {
                            stuScoreLog.setDescription("超出次数上限，本次不计入总分");
                        }
                        stuScoreLogMapper.insert(stuScoreLog);
                    }
                } else {
                    // 有次数限制  扣分
                    for (Long studentId : studentIds) {
                        Integer useCollectCount = studentCollectLimitMap.getOrDefault(studentId, 0);
                        if (useCollectCount < collectLimit) {
                            RecDeductScore recDeductScore = new RecDeductScore();
                            recDeductScore.setYearId(yearId);
                            recDeductScore.setSemesterId(semesterId);
                            recDeductScore.setStudentId(studentId);
                            recDeductScore.setGrowId(growthItemId);
                            recDeductScore.setCreateTime(time);
                            recDeductScore.setScore(growthItemScore);
                            recDeductScoreMapper.insert(recDeductScore);
                            studentCollectLimitMap.put(studentId, useCollectCount + 1);
                        }
                        StuScoreLog stuScoreLog = new StuScoreLog();
                        stuScoreLog.setYearId(yearId);
                        stuScoreLog.setSemesterId(semesterId);
                        stuScoreLog.setStudentId(studentId);
                        stuScoreLog.setGrowId(growthItemId);
                        stuScoreLog.setCreateTime(time);
                        stuScoreLog.setScore(growthItemScore.negate());
                        stuScoreLogMapper.insert(stuScoreLog);
                    }
                    HashSet<Long> studentIdSet = new HashSet<>(studentIds);
                    List<Long> allStudentIds = studentMapper.selectIdList();
                    for (Long studentId : allStudentIds) {
                        Integer useCollectCount = studentCollectLimitMap.getOrDefault(studentId, 0);
                        StuScoreLog stuScoreLog = new StuScoreLog();
                        stuScoreLog.setYearId(yearId);
                        stuScoreLog.setSemesterId(semesterId);
                        stuScoreLog.setStudentId(studentId);
                        stuScoreLog.setGrowId(growthItemId);
                        stuScoreLog.setCreateTime(time);
                        stuScoreLog.setScore(growthItemScore);
                        if (useCollectCount < collectLimit && !studentIdSet.contains(studentId)) {
                            RecAddScore recAddScore = new RecAddScore();
                            recAddScore.setYearId(yearId);
                            recAddScore.setSemesterId(semesterId);
                            recAddScore.setStudentId(studentId);
                            recAddScore.setGrowId(growthItemId);
                            recAddScore.setCreateTime(time);
                            recAddScore.setScore(growthItemScore);
                            recAddScoreMapper.insert(recAddScore);
                            stuScoreService.updateTotalScore(studentId, growthItemScore);
                            studentCollectLimitMap.put(studentId, useCollectCount + 1);
                        } else {
                            stuScoreLog.setDescription("超出次数上限，本次不计入总分");
                        }
                        stuScoreLogMapper.insert(stuScoreLog);
                    }
                }
                studentCollectLimitMap.forEach((studentId, count) -> {
                    StuGrowth stuGrowth = stuGrowthService.getOne(Wrappers.<StuGrowth>lambdaQuery()
                            .eq(StuGrowth::getStudentId, studentId)
                            .eq(StuGrowth::getGrowId, growthItemId));
                    if (stuGrowth == null) {
                        StuGrowth insertSstuGrowth = new StuGrowth();
                        insertSstuGrowth.setStudentId(studentId);
                        insertSstuGrowth.setGrowId(growthItemId);
                        insertSstuGrowth.setCount(count);
                        stuGrowthMapper.insert(insertSstuGrowth);
                    } else {
                        stuGrowth.setCount(count);
                        stuGrowthService.updateById(stuGrowth);
                    }
                });
            } else {
                if (isPlusCalculateType) {
                    // 无次数限制  加分
                    for (Long studentId : studentIds) {
                        // 有次数限制  加分
                        RecAddScore recAddScore = new RecAddScore();
                        recAddScore.setYearId(yearId);
                        recAddScore.setSemesterId(semesterId);
                        recAddScore.setStudentId(studentId);
                        recAddScore.setGrowId(growthItemId);
                        recAddScore.setCreateTime(time);
                        recAddScore.setScore(growthItemScore);
                        recAddScoreMapper.insert(recAddScore);
                        StuScoreLog stuScoreLog = new StuScoreLog();
                        stuScoreLog.setYearId(yearId);
                        stuScoreLog.setSemesterId(semesterId);
                        stuScoreLog.setStudentId(studentId);
                        stuScoreLog.setGrowId(growthItemId);
                        stuScoreLog.setCreateTime(time);
                        stuScoreLog.setScore(growthItemScore);
                        stuScoreLogMapper.insert(stuScoreLog);
                        stuScoreService.updateTotalScore(studentId, growthItemScore);
                    }
                } else {
                    // 无次数限制  扣分
                    for (Long studentId : studentIds) {
                        RecDeductScore recDeductScore = new RecDeductScore();
                        recDeductScore.setYearId(yearId);
                        recDeductScore.setSemesterId(semesterId);
                        recDeductScore.setStudentId(studentId);
                        recDeductScore.setGrowId(growthItemId);
                        recDeductScore.setCreateTime(time);
                        recDeductScore.setScore(growthItemScore);
                        recDeductScoreMapper.insert(recDeductScore);
                        StuScoreLog stuScoreLog = new StuScoreLog();
                        stuScoreLog.setYearId(yearId);
                        stuScoreLog.setSemesterId(semesterId);
                        stuScoreLog.setStudentId(studentId);
                        stuScoreLog.setGrowId(growthItemId);
                        stuScoreLog.setCreateTime(time);
                        stuScoreLog.setScore(growthItemScore.negate());
                        stuScoreLogMapper.insert(stuScoreLog);
                    }
                    HashSet<Long> studentIdSet = new HashSet<>(studentIds);
                    List<Long> allStudentIds = studentMapper.selectIdList();
                    for (Long studentId : allStudentIds) {
                        if (!studentIdSet.contains(studentId)) {
                            RecAddScore recAddScore = new RecAddScore();
                            recAddScore.setYearId(yearId);
                            recAddScore.setSemesterId(semesterId);
                            recAddScore.setStudentId(studentId);
                            recAddScore.setGrowId(growthItemId);
                            recAddScore.setCreateTime(time);
                            recAddScore.setScore(growthItemScore);
                            recAddScoreMapper.insert(recAddScore);
                            StuScoreLog stuScoreLog = new StuScoreLog();
                            stuScoreLog.setYearId(yearId);
                            stuScoreLog.setSemesterId(semesterId);
                            stuScoreLog.setStudentId(studentId);
                            stuScoreLog.setGrowId(growthItemId);
                            stuScoreLog.setCreateTime(time);
                            stuScoreLog.setScore(growthItemScore);
                            stuScoreLogMapper.insert(stuScoreLog);
                            stuScoreService.updateTotalScore(studentId, growthItemScore);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void calculateStudentScore(Long growId, Long studentId) {
        GrowthItem growthItem = growthItemService.getById(growId);
        if (growthItem == null) throw new RuntimeException("未查询到申请的项目");
        Integer scorePeriod = growthItem.getScorePeriod();    // 积分刷新周期
        PeriodEnum periodEnum = PeriodEnum.getByValue(scorePeriod);
        if (periodEnum == null) throw new RuntimeException("项目积分刷新周期设置错误，请联系管理员");
        Year year = yearMapper.getCurrentYear();
        if (year == null) throw new RuntimeException("不在学年时间范围内，无法完成计算");
        Long yearId = year.getOid();
        Long semesterId = semesterMapper.getCurrentSemesterId();
        RecDefault recDefault = new RecDefault();
        recDefault.setYearId(yearId);
        recDefault.setSemesterId(semesterId);
        recDefault.setStudentId(studentId);
        recDefault.setGrowId(growId);
        recDefault.setBatchCode(System.currentTimeMillis());
        recDefaultService.save(recDefault);
        if (PeriodEnum.UNLIMITED.getValue() == scorePeriod) {
            Integer calculateType = growthItem.getCalculateType();
            Integer collectLimit = growthItem.getCollectLimit();
            if (calculateType != CalculateTypeEnum.PLUS.getValue())
                throw new RuntimeException("学生申请不能未扣分项目");
            if (collectLimit != null && collectLimit > 0) {
                StuGrowth stuGrowth = stuGrowthMapper.selectOne(Wrappers.<StuGrowth>lambdaQuery()
                        .select(StuGrowth::getCount)
                        .eq(StuGrowth::getGrowId, growthItem.getId())
                        .eq(StuGrowth::getStudentId, studentId));
                Integer count = stuGrowth.getCount();
                if (count >= collectLimit) throw new RuntimeException("该学生项目申请已达设置次数，无法再次申请");
            }
            RecAddScore recAddScore = new RecAddScore();
            recAddScore.setStudentId(studentId);
            recAddScore.setGrowId(growthItem.getId());
            recAddScore.setScore(growthItem.getScore());
            recAddScore.setYearId(yearId);
            recAddScore.setSemesterId(semesterId);
            this.save(recAddScore);
            // 添加积分日志
            StuScoreLog stuScoreLog = new StuScoreLog();
            stuScoreLog.setGrowId(growthItem.getId());
            stuScoreLog.setStudentId(studentId);
            stuScoreLog.setScore(growthItem.getScore());
            stuScoreLog.setYearId(yearId);
            stuScoreLog.setSemesterId(semesterId);
            stuScoreLogService.save(stuScoreLog);
            // 更新总分
            stuScoreService.updateTotalScore(studentId, growthItem.getScore());
        } else {
            log.info("项目积分刷新周期为：{}，延后至定时脚本计算", periodEnum.getKey());
        }
    }

    @Override
    public Integer getStudentNowRanking(Long studentId) {
        Long yearId = yearMapper.getCurrentYearId();
        if (yearId != null) {
            List<RecAddScore> recAddScores = baseMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                    .select(RecAddScore::getStudentId, RecAddScore::getScore)
                    .eq(RecAddScore::getYearId, yearId)
                    .groupBy(RecAddScore::getStudentId));
            if (CollUtil.isNotEmpty(recAddScores)) {
                // 对recAddScores数组按照score字段从高到低排序
                recAddScores.sort(Comparator.comparing(RecAddScore::getScore).reversed());

                // 设置排名
                int rank = 1;
                BigDecimal lastStudentScore = recAddScores.get(0).getScore();
                Integer studentRank = null;

                for (RecAddScore recAddScore : recAddScores) {
                    if (lastStudentScore.compareTo(recAddScore.getScore()) != 0) {
                        rank++;
                    }
                    if (recAddScore.getStudentId().equals(studentId)) {
                        studentRank = rank;
                        break;
                    }
                    lastStudentScore = recAddScore.getScore();
                }

                return studentRank;
            }
        }
        return null;
    }

    @Override
    public void collectionTimeoutScore(Integer timeout) {
        baseMapper.collectionTimeoutScore(timeout);
    }

    @Override
    public List<UnCollectScore> getUnCollectScore(Long userId) {
        List<UnCollectScore> result = new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(userId);
        if (user != null) {
            Student student = studentMapper.getStudentForStudentNO(user.getLoginName());
            if (student != null) {
                Integer studentId = student.getId();
                List<RecAddScore> unCollectScores = baseMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                        .select(RecAddScore::getId, RecAddScore::getScore)
                        .eq(RecAddScore::getStudentId, studentId)
                        .eq(RecAddScore::getState, WhetherEnum.NO.getValue()));
                for (RecAddScore recAddScore : unCollectScores) {
                    UnCollectScore unCollectScore = new UnCollectScore();
                    unCollectScore.setId(recAddScore.getId());
                    unCollectScore.setScore(recAddScore.getScore());
                    result.add(unCollectScore);
                }
                return result;
            }
        }
        return result;
    }


}
