package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.event.EventPublish;
import com.poho.stuup.event.StatisticsMonthRankEvent;
import com.poho.stuup.event.StatisticsSemesterRankEvent;
import com.poho.stuup.event.StatisticsYearRankEvent;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.StatisticsRankEventDTO;
import com.poho.stuup.model.dto.StudentRecScoreDTO;
import com.poho.stuup.model.dto.TimePeriod;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.service.*;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class RecScoreServiceImpl extends ServiceImpl<RecScoreMapper, RecScore> implements RecScoreService {

    @Resource
    private EventPublish eventPublish;

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


    @Override
    public IPage<RecScoreVO> getRecScorePage(Page<RecScoreVO> page, RecScoreDTO query) {
        return baseMapper.getRecScorePage(page, query);
    }

    @Override
    public ResponseModel<IPage<StudentRecScoreVO>> pageStudentRecScore(Page<StudentRecScoreVO> page, Long userId, StudentRecScoreDTO query) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ResponseModel.failed("未查询到您的用户信息，请联系管理员");
        String loginName = user.getLoginName();
        Long studentId = studentMapper.findStudentId(loginName);
        if (studentId == null) return ResponseModel.failed("未查询到您的学生信息，请联系管理员");
        return ResponseModel.ok(baseMapper.pageStudentRecScore(page, studentId, query));
    }

    @Override
    public void calculateScore(List<RecDefault> recDefaults, Long yearId, GrowthItem growthItem) {
        if (CollUtil.isEmpty(recDefaults)) {
            log.warn("需要计算对象为空，结束执行");
            return;
        }
        Long semesterId = semesterMapper.getCurrentSemesterId();
        // 保存导入记录
        recDefaultService.saveBatch(recDefaults);
        List<Long> defaultStudentIds = recDefaults.stream().map(RecDefault::getStudentId).collect(Collectors.toList());
        Set<Long> allStudentIds = studentMapper.selectIdList();
        Integer calculateType = growthItem.getCalculateType();
        // 获取项目积分刷新周期
        Integer scorePeriod = growthItem.getScorePeriod();    // 积分刷新周期
        PeriodEnum periodEnum = PeriodEnum.getByValue(scorePeriod);
        if (periodEnum == null) throw new RuntimeException("项目积分刷新周期设置错误，请联系管理员");
        if (PeriodEnum.UNLIMITED.getValue() == scorePeriod) {
            if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
                defaultStudentIds.forEach(studentId -> {
                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItem.getId());
                    recScore.setScore(growthItem.getScore());
                    recScore.setYearId(yearId);
                    recScore.setSemesterId(semesterId);
                    this.save(recScore);
                    // 添加积分日志
                    StuScoreLog stuScoreLog = new StuScoreLog();
                    stuScoreLog.setGrowId(growthItem.getId());
                    stuScoreLog.setStudentId(studentId);
                    stuScoreLog.setScore(growthItem.getScore());
                    stuScoreLog.setYearId(yearId);
                    stuScoreLog.setSemesterId(semesterId);
                    stuScoreLogService.save(stuScoreLog);
                    // 更新总积分
                    stuScoreService.updateTotalScore(studentId, growthItem.getScore());
                });
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
                allStudentIds.forEach(studentId -> {
                    StuScoreLog stuScoreLog = new StuScoreLog();
                    stuScoreLog.setGrowId(growthItem.getId());
                    stuScoreLog.setStudentId(studentId);
                    stuScoreLog.setScore(growthItem.getScore());
                    stuScoreLog.setYearId(yearId);
                    stuScoreLog.setSemesterId(semesterId);
                    if (defaultStudentIds.contains(studentId)) {
                        // 添加积分日志
                        stuScoreLog.setScore(growthItem.getScore().negate());
                        stuScoreLogService.save(stuScoreLog);
                        return;
                    }
                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItem.getId());
                    recScore.setScore(growthItem.getScore());
                    recScore.setYearId(yearId);
                    recScore.setSemesterId(semesterId);
                    this.save(recScore);
                    // 添加积分日志
                    stuScoreLogService.save(stuScoreLog);
                    // 更新总分
                    stuScoreService.updateTotalScore(studentId, growthItem.getScore());
                });
            } else {
                log.error("项目积分计算类型不存在：”{}“，请检查项目”{}“配置", calculateType, growthItem.getName());
            }
        } else {
            log.info("项目积分刷新周期为：{}，延后至定时脚本计算", periodEnum.getKey());
        }
    }


    /**
     * @description: 计算有效积分
     * @param: studentScore     学生当前积分
     * @param: growScore        项目可获取积分
     * @param: scoreUpperLimit  项目积分上限
     * @return: int
     * @author BUNGA
     * @date: 2023/6/6 14:47
     */
    private BigDecimal calculateEffectiveScore(BigDecimal studentScore, BigDecimal growScore, BigDecimal scoreUpperLimit) {
        // 溢出积分 = （学生当前项目总积分 + 项目单次可获得积分） - 项目积分上限
        BigDecimal overflowScore = studentScore.add(growScore).subtract(scoreUpperLimit).max(BigDecimal.ZERO);
        // 有效积分
        BigDecimal effectiveScore;
        if (overflowScore.compareTo(BigDecimal.ZERO) > 0) {    //分数溢出
            // 有效分数 = 项目可获得分数 - 溢出分数 (为负数返回0)
            effectiveScore = growScore.subtract(overflowScore).max(BigDecimal.ZERO);
        } else {    // 分数未溢出
            // 有效分数 = 项目单次可获得积分
            effectiveScore = growScore;
        }
        return effectiveScore;
    }

    @Override
    public Map<Long, BigDecimal> findTimePeriodScoreMap(Date startTime, Date endTime) {
        List<RecScore> recScores = baseMapper.findTimePeriodRecord(startTime, endTime);
        return recScores.stream().collect(Collectors.groupingBy(RecScore::getStudentId, Collectors.reducing(BigDecimal.ZERO, RecScore::getScore, BigDecimal::add)));
    }

    @Override
    public Map<Long, BigDecimal> findTimePeriodScoreMap(Long growthId, Date startTime, Date endTime) {
        List<RecScore> recScores = baseMapper.findTimePeriodRecordForGrow(growthId, startTime, endTime);
        return recScores.stream().collect(Collectors.groupingBy(RecScore::getStudentId, Collectors.reducing(BigDecimal.ZERO, RecScore::getScore, BigDecimal::add)));
    }

    @Override
    public void calculateStudentScore(Long growId, Long studentId) {
        GrowthItem growthItem = growthItemService.getById(growId);
        if (growthItem == null) throw new RuntimeException("未查询到申请的项目");
        Integer scorePeriod = growthItem.getScorePeriod();    // 积分刷新周期
        PeriodEnum periodEnum = PeriodEnum.getByValue(scorePeriod);
        if (periodEnum == null) throw new RuntimeException("项目积分刷新周期设置错误，请联系管理员");
        Year year = yearMapper.findCurrYear();
        Long yearId = year.getOid();
        Long semesterId = semesterMapper.getCurrentSemesterId();
        RecDefault recDefault = new RecDefault();
        recDefault.setYearId(yearId);
        recDefault.setStudentId(studentId);
        recDefault.setGrowId(growId);
        recDefault.setBatchCode(System.currentTimeMillis());
        recDefaultService.save(recDefault);
        if (PeriodEnum.UNLIMITED.getValue() == scorePeriod) {
            Integer calculateType = growthItem.getCalculateType();
            if (calculateType != CalculateTypeEnum.PLUS.getValue())
                throw new RuntimeException("当前项目设置计算方式设置错误，设置为扣分这不会有人申请，这不符合逻辑，请联系管理员修改");
            RecScore recScore = new RecScore();
            recScore.setStudentId(studentId);
            recScore.setGrowId(growthItem.getId());
            recScore.setScore(growthItem.getScore());
            recScore.setYearId(yearId);
            recScore.setSemesterId(semesterId);
            this.save(recScore);
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
    @Transactional(rollbackFor = Exception.class)
    public void calculateScore(PeriodEnum periodEnum) {
        log.info("========================开始计算学生积分========================");
        Date date = new Date();
        TimePeriod timePeriod = Utils.getDateTimePeriod(periodEnum, date);
        if (timePeriod == null) throw new RuntimeException("上下学期起止时间配置不存在或者错误,请联系管理员");
        long start = System.currentTimeMillis();

        int periodEnumValue = periodEnum.getValue();
        Year currYear = yearMapper.findCurrYear();
        if (currYear == null) {
            log.info("不在学年时间范围内，结束执行");
            return;
        }
        Long yearId = currYear.getOid();
        Long semesterId = semesterMapper.getCurrentSemesterId();

        // 查询对应的项目
        List<GrowthItem> growthItems = growthItemService.list(Wrappers.<GrowthItem>lambdaQuery().eq(GrowthItem::getScorePeriod, periodEnumValue));

        // 所有学生id
        Set<Long> allStudentIds = studentMapper.selectIdList();

        for (GrowthItem growthItem : growthItems) {
            Long growthItemId = growthItem.getId();
            // 查询该项目的导入的记录
            List<Long> defaultStudentIds = recDefaultService.listObjs(Wrappers.<RecDefault>lambdaQuery()
                            .select(RecDefault::getStudentId)
                            .between(RecDefault::getCreateTime, timePeriod.getStartTime(), timePeriod.getEndTime()),
                    studentId -> Long.valueOf(studentId.toString()));

            // TODO 测试
//            if (CollUtil.isEmpty(defaultStudentIds)) continue;

            Integer calculateType = growthItem.getCalculateType();
            BigDecimal scoreUpperLimit = growthItem.getScoreUpperLimit();  // 项目积分上限
            BigDecimal score = growthItem.getScore();                      // 项目单次可获得积分
            if (score.compareTo(scoreUpperLimit) > 0) {
                log.error("{}-项目设置不合法", growthItem.getName());
                throw new RuntimeException(StrUtil.format("{}-项目设置不合法", growthItem.getName()));
            }

            // 保存学生和对应的积分
            Map<Long, BigDecimal> studentScoreMap = new HashMap<>();

            if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
                // 导入为加分的
                defaultStudentIds.forEach(studentId -> {
                    StuScoreLog stuScoreLog = new StuScoreLog();
                    stuScoreLog.setGrowId(growthItem.getId());
                    stuScoreLog.setStudentId(studentId);
                    stuScoreLog.setScore(score);
                    stuScoreLog.setYearId(yearId);
                    stuScoreLog.setSemesterId(semesterId);

                    BigDecimal studentScore = studentScoreMap.getOrDefault(studentId, BigDecimal.ZERO); // 学生当前项目总积分
                    // 计算有效积分
                    BigDecimal effectiveScore = calculateEffectiveScore(studentScore, score, scoreUpperLimit);    // 有效积分

                    // 有效积分大于0才添加获得积分的记录
                    if (effectiveScore.compareTo(BigDecimal.ZERO) > 0) {
                        RecScore recScore = new RecScore();
                        recScore.setStudentId(studentId);
                        recScore.setGrowId(growthItemId);
                        recScore.setYearId(yearId);
                        recScore.setSemesterId(semesterId);
                        recScore.setScore(effectiveScore);
                        this.save(recScore);
                        // 并且更新总分 + 有效分
                        stuScoreService.updateTotalScore(studentId, effectiveScore);
                    } else { // 否则积分无效，添加说明
                        stuScoreLog.setDescription("已超出可获取成长值上限，本次不计入总成长值");
                    }
                    // 添加加分日志
                    stuScoreLogService.save(stuScoreLog);

                    // 更新map为 学生当前项目总积分 (+ 有效积分)
                    studentScoreMap.put(studentId, studentScore.add(effectiveScore));
                });
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
                // 保存扣分学生和对应的总扣分
                Map<Long, BigDecimal> studentSubtractScoreMap = new HashMap<>();
                // 计算录入扣分的同学总扣分
                defaultStudentIds.forEach(studentId -> {
                    // 总扣分
                    BigDecimal totalSubtractScore = studentSubtractScoreMap.getOrDefault(studentId, BigDecimal.ZERO);
                    // 更新总扣分
                    studentSubtractScoreMap.put(studentId, totalSubtractScore.add(score));
                    // 保存扣分日志
                    StuScoreLog stuScoreLog = new StuScoreLog();
                    stuScoreLog.setStudentId(studentId);
                    stuScoreLog.setGrowId(growthItemId);
                    stuScoreLog.setYearId(yearId);
                    stuScoreLog.setSemesterId(semesterId);
                    stuScoreLog.setScore(score.negate()); //取反
                    if (totalSubtractScore.compareTo(scoreUpperLimit) > 0) {
                        // 总扣分大于上限，添加说明
                        stuScoreLog.setDescription("已超出扣除成长值上限，本次扣除不计入总成长值");
                    }
                    stuScoreLogService.save(stuScoreLog);
                });
                // 对扣分同学的数组进行去重，防止重复添加剩余分
                Set<Long> defaultStudentIdSet = new HashSet<>(defaultStudentIds);
                // 扣分的同学计算剩余可加的分
                defaultStudentIdSet.forEach(studentId -> {
                    // 总扣分
                    BigDecimal totalSubtractScore = studentSubtractScoreMap.getOrDefault(studentId, BigDecimal.ZERO);
                    if (totalSubtractScore.compareTo(scoreUpperLimit) < 0) {
                        // 剩余可加分
                        BigDecimal residualAddScore = scoreUpperLimit.subtract(totalSubtractScore);
                        // 总扣分小于上限，添加获取积分记录
                        RecScore recScore = new RecScore();
                        recScore.setStudentId(studentId);
                        recScore.setGrowId(growthItemId);
                        recScore.setYearId(yearId);
                        recScore.setSemesterId(semesterId);
                        recScore.setScore(residualAddScore);    // 上限减去扣除
                        this.save(recScore);
                        // 并且更新总分 (+ 剩余分)
                        stuScoreService.updateTotalScore(studentId, residualAddScore);
                        // 添加加分日志
                        StuScoreLog stuScoreLog = new StuScoreLog();
                        stuScoreLog.setStudentId(studentId);
                        stuScoreLog.setGrowId(growthItemId);
                        stuScoreLog.setYearId(yearId);
                        stuScoreLog.setSemesterId(semesterId);
                        stuScoreLog.setScore(residualAddScore);
                        stuScoreLog.setDescription(StrUtil.format("成长值来源：{}项目采集周期内默认分：{}分 - 违规总扣除：{}分", growthItem.getName(), scoreUpperLimit, totalSubtractScore));
                        stuScoreLogService.save(stuScoreLog);
                    }
                });
                studentSubtractScoreMap.clear();
                // 排除扣分的同学，其他全部计算加分
                allStudentIds.forEach(studentId -> {
                    if (defaultStudentIdSet.contains(studentId)) return;
                    // 不存在重复的同学，不需要计算积分是否超上限，直接添加记录并更新总分
                    // 添加加分记录
                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItemId);
                    recScore.setYearId(yearId);
                    recScore.setSemesterId(semesterId);
                    recScore.setScore(scoreUpperLimit);
                    this.save(recScore);
                    // 添加加分日志
                    StuScoreLog stuScoreLog = new StuScoreLog();
                    stuScoreLog.setGrowId(growthItemId);
                    stuScoreLog.setStudentId(studentId);
                    stuScoreLog.setScore(scoreUpperLimit);
                    stuScoreLog.setYearId(yearId);
                    stuScoreLog.setSemesterId(semesterId);
                    stuScoreLog.setDescription(StrUtil.format("成长值来源:{}，无违规默认分：+{}分", growthItem.getName(), scoreUpperLimit));
                    stuScoreLogService.save(stuScoreLog);
                    // 并且更新总分（+项目上限积分）
                    stuScoreService.updateTotalScore(studentId, scoreUpperLimit);
                });
            } else {
                log.error("项目积分计算类型不存在：”{}“，请检查项目”{}“配置", calculateType, growthItem.getName());
            }
            studentScoreMap.clear();
        }
        growthItems.clear();
        allStudentIds.clear();

        long end = System.currentTimeMillis();
        log.info("========================计算学生积分已完成========================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);

        // 统计排行榜
        StatisticsRankEventDTO statisticsRankEventDTO = new StatisticsRankEventDTO();
        statisticsRankEventDTO.setPeriodEnum(periodEnum);
        statisticsRankEventDTO.setYearId(yearId);
        statisticsRankEventDTO.setSemesterId(semesterId);
        statisticsRankEventDTO.setStartTime(date);


        switch (periodEnum) {
            case YEAR:
                eventPublish.publishEvent(new StatisticsYearRankEvent(statisticsRankEventDTO));
                break;
            case SEMESTER:
                eventPublish.publishEvent(new StatisticsSemesterRankEvent(statisticsRankEventDTO));
                break;
            case MONTH:
                eventPublish.publishEvent(new StatisticsMonthRankEvent(statisticsRankEventDTO));
                break;
            default:
                break;
        }
    }

}
