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
import com.poho.stuup.dao.RecScoreMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.event.EventPublish;
import com.poho.stuup.event.MonthRankingEvent;
import com.poho.stuup.event.YearRankingEvent;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.StudentRecScoreDTO;
import com.poho.stuup.model.dto.TimePeriod;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.service.RecDefaultService;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // 保存导入记录
        recDefaultService.saveBatch(recDefaults);
        List<Long> defaultStudentIds = recDefaults.stream().map(RecDefault::getStudentId).collect(Collectors.toList());
        List<Long> allStudentIds = studentMapper.selectIdList();
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
                    this.save(recScore);
                    stuScoreService.updateTotalScore(studentId, growthItem.getScore());
                });
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
                allStudentIds.forEach(studentId -> {
                    if (defaultStudentIds.contains(studentId)) return;
                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItem.getId());
                    recScore.setScore(growthItem.getScore());
                    recScore.setYearId(yearId);
                    this.save(recScore);
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
        RecDefault recDefault = new RecDefault();
        recDefault.setYearId(yearId);
        recDefault.setStudentId(studentId);
        recDefault.setGrowId(growId);
        recDefault.setBatchCode(System.currentTimeMillis());
        recDefaultService.save(recDefault);
        if (PeriodEnum.UNLIMITED.getValue() == scorePeriod) {
            // 忽略计算类型
            RecScore recScore = new RecScore();
            recScore.setStudentId(studentId);
            recScore.setGrowId(growthItem.getId());
            recScore.setScore(growthItem.getScore());
            recScore.setYearId(yearId);
            this.save(recScore);
            stuScoreService.updateTotalScore(studentId, growthItem.getScore());
        } else {
            log.info("项目积分刷新周期为：{}，延后至定时脚本计算", periodEnum.getKey());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateScore(PeriodEnum periodEnum) {
        Date nowData = new Date();
        log.info("========================开始计算学生积分========================");
        long start = System.currentTimeMillis();

        int periodEnumValue = periodEnum.getValue();
        Year currYear = yearMapper.findCurrYear();
        Long yearId = currYear.getOid();

        // 查询对应的项目
        List<GrowthItem> growthItems = growthItemService.list(Wrappers.<GrowthItem>lambdaQuery().eq(GrowthItem::getScorePeriod, periodEnumValue));

        for (GrowthItem growthItem : growthItems) {
            Long growthItemId = growthItem.getId();
            TimePeriod timePeriod = Utils.getCurrentTimePeriod(periodEnum);
            // 查询该项目的导入的记录
            List<Long> defaultStudentIds = recDefaultService.findGrowStudentRecForTimePeriod(growthItem.getId(), timePeriod.getStartTime(), timePeriod.getEndTime());

            if (CollUtil.isEmpty(defaultStudentIds)) return;    //TODO 用于测试跳过其他项目计算

            // 所有学生id
            List<Long> allStudentIds = studentMapper.selectIdList();

            Integer calculateType = growthItem.getCalculateType();
            BigDecimal scoreUpperLimit = growthItem.getScoreUpperLimit();  // 项目积分上限
            BigDecimal score = growthItem.getScore();                      // 项目单次可获得积分
            if (score.compareTo(scoreUpperLimit) > 0) {
                log.error("{}-项目设置不合法", growthItem.getName());
                throw new RuntimeException(StrUtil.format("{}-项目设置不合法", growthItem.getName()));
            }

            // 保存学生和对应的积分
            Map<Long, BigDecimal> studentScoreMap = new HashMap<>();

            // 保存学生积分获取记录
            if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
                // 导入为计分的
                defaultStudentIds.forEach(studentId -> {
                    BigDecimal studentScore = studentScoreMap.getOrDefault(studentId, BigDecimal.ZERO); // 学生当前项目总积分
                    BigDecimal effectiveScore = calculateEffectiveScore(studentScore, score, scoreUpperLimit);    // 有效积分

                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItemId);
                    recScore.setYearId(yearId);
                    recScore.setScore(effectiveScore);
                    this.save(recScore);

                    // 更新map为 学生当前项目总积分 + 有效积分
                    studentScoreMap.put(studentId, studentScore.add(effectiveScore));
                });
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
                // 导入为不计分的排除在 defaultStudentIds 里面的学生
                allStudentIds.forEach(studentId -> {
                    if (defaultStudentIds.contains(studentId)) return;
                    BigDecimal studentScore = studentScoreMap.getOrDefault(studentId, BigDecimal.ZERO); // 学生当前项目总积分
                    BigDecimal effectiveScore = calculateEffectiveScore(studentScore, score, scoreUpperLimit);

                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItemId);
                    recScore.setYearId(yearId);
                    recScore.setScore(effectiveScore);
                    this.save(recScore);

                    // 更新map为 学生当前项目总积分 + 有效积分
                    studentScoreMap.put(studentId, studentScore.add(effectiveScore));
                });
            } else {
                log.error("项目积分计算类型不存在：”{}“，请检查项目”{}“配置", calculateType, growthItem.getName());
            }
        }

        long end = System.currentTimeMillis();
        log.info("========================计算学生积分已完成========================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);

        // 统计排行榜
        if (periodEnum == PeriodEnum.YEAR) {
            eventPublish.publishEvent(new YearRankingEvent(yearId));
        }
        if (periodEnum == PeriodEnum.MONTH) {
            eventPublish.publishEvent(new MonthRankingEvent(nowData));
        }
    }

}
