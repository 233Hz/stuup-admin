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
import com.poho.stuup.event.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.*;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.service.*;
import com.poho.stuup.util.Utils;
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

    @Resource
    private CalculateScoreService calculateScoreService;

    @Resource
    private StuGrowthMapper stuGrowthMapper;

    @Resource
    private StuGrowthService stuGrowthService;

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
    public void calculateScore(List<RecDefault> recDefaults, Long yearId, GrowthItem growthItem, Map<String, Object> params) {
        // 检查recDefaults是否为空
        if (CollUtil.isEmpty(recDefaults)) {
            log.warn("计算对象为空，结束执行");
            return;
        }

        Date nowTime = (Date) params.get("nowTime");
        Long semesterId = semesterMapper.getCurrentSemesterId();
        if (semesterId == null) {
            throw new RuntimeException("不在学期时间范围内，无法导入");
        }

        // 保存导入记录
        recDefaultService.saveBatch(recDefaults);

        // 获取计算类型和积分刷新周期
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

        // 根据积分刷新周期进行积分计算
        if (PeriodEnum.UNLIMITED.getValue() == scorePeriod) {
            // 统计每个学生出现的次数
            Map<Long, Integer> studentFrequencyMap = new HashMap<>();
            int recDefaultSize = recDefaults.size();
            for (int i = 0; i < recDefaultSize; i++) {
                RecDefault recDefault = recDefaults.get(i);
                Long studentId = recDefault.getStudentId();
                Integer count = studentFrequencyMap.get(studentId);
                if (count == null) {
                    studentFrequencyMap.put(studentId, 1);
                } else {
                    studentFrequencyMap.put(studentId, count + 1);
                }
            }
            if (hasCollectLimit) {  // 有次数限制
                // 获取学生的积分采集次数信息
                List<StuGrowth> stuGrowths = stuGrowthMapper.selectList(Wrappers.<StuGrowth>lambdaQuery()
                        .select(StuGrowth::getStudentId, StuGrowth::getCount)
                        .eq(StuGrowth::getGrowId, growthItemId));
                Map<Long, Integer> studentCollectLimitMap = stuGrowths.stream().collect(Collectors.toMap(StuGrowth::getStudentId, StuGrowth::getCount));
                if (isPlusCalculateType) {  // 加分
                    for (Map.Entry<Long, Integer> entry : studentFrequencyMap.entrySet()) {
                        Long studentId = entry.getKey();
                        Integer count = entry.getValue();    //当前出现次数
                        Integer useCount = studentCollectLimitMap.getOrDefault(studentId, 0);   //已经出现次数
                        savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, nowTime, count, useCount, collectLimit, growthItemScore);
                    }
                } else {    // 扣分
                    List<Long> allStudentIds = studentMapper.selectIdList();
                    Set<Long> defaultStudentIdSet = recDefaults.stream().map(RecDefault::getStudentId).collect(Collectors.toSet());
                    int size = allStudentIds.size();
                    for (int i = 0; i < size; i++) {
                        Long studentId = allStudentIds.get(i);
                        Integer count = studentFrequencyMap.getOrDefault(studentId, 1); //当前出现次数
                        Integer useCount = studentCollectLimitMap.getOrDefault(studentId, 0);   //已经出现次数
                        if (defaultStudentIdSet.contains(studentId)) {
                            saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, nowTime, count, useCount, collectLimit, growthItemScore);
                        } else {
                            savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, nowTime, count, useCount, collectLimit, growthItemScore);
                        }

                    }
                }
            } else { // 无次数限制
                if (isPlusCalculateType) {  //加分
                    for (Map.Entry<Long, Integer> entry : studentFrequencyMap.entrySet()) {
                        Long studentId = entry.getKey();
                        Integer count = entry.getValue();    //当前出现次数
                        savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, nowTime, count, growthItemScore);
                    }
                } else {    // 扣分
                    List<Long> allStudentIds = studentMapper.selectIdList();
                    Map<Long, BigDecimal> studentMinusScoreMap = new HashMap<>();
                    int r_size = recDefaults.size();
                    for (int i = 0; i < r_size; i++) {
                        RecDefault recDefault = recDefaults.get(i);
                        Long studentId = recDefault.getStudentId();
                        BigDecimal totalMinusScore = studentMinusScoreMap.getOrDefault(studentId, BigDecimal.ZERO);
                        totalMinusScore = totalMinusScore.add(growthItemScore);
                        studentMinusScoreMap.put(studentId, totalMinusScore);
                    }
                    Set<Long> defaultStudentIdSet = recDefaults.stream().map(RecDefault::getStudentId).collect(Collectors.toSet());
                    int size = allStudentIds.size();
                    for (int i = 0; i < size; i++) {
                        Long studentId = allStudentIds.get(i);
                        Integer count = studentFrequencyMap.getOrDefault(studentId, 1); //当前出现次数
                        if (defaultStudentIdSet.contains(studentId)) {
                            saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, nowTime, count, growthItemScore);
                        } else {
                            savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, nowTime, count, growthItemScore);
                        }
                    }
                }
            }

        } else {
            // 其他刷新周期暂不处理，延后至定时脚本计算
            log.info("项目积分刷新周期为：{}，延后至定时脚本计算", periodEnum.getKey());
        }
    }


    @Override
    public void calculateStudentScore(Long growId, Long studentId) {
        GrowthItem growthItem = growthItemService.getById(growId);
        if (growthItem == null) throw new RuntimeException("未查询到申请的项目");
        Integer scorePeriod = growthItem.getScorePeriod();    // 积分刷新周期
        PeriodEnum periodEnum = PeriodEnum.getByValue(scorePeriod);
        if (periodEnum == null) throw new RuntimeException("项目积分刷新周期设置错误，请联系管理员");
        Year year = yearMapper.findCurrYear();
        if (year == null) throw new RuntimeException("不在学年时间范围内，无法完成计算");
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
            Integer collectLimit = growthItem.getCollectLimit();
            if (calculateType != CalculateTypeEnum.PLUS.getValue())
                throw new RuntimeException("当前项目设置计算方式设置为扣分不符合逻辑，请联系管理员修改");
            if (collectLimit != null && collectLimit > 0) {
                StuGrowth stuGrowth = stuGrowthMapper.selectOne(Wrappers.<StuGrowth>lambdaQuery()
                        .select(StuGrowth::getCount)
                        .eq(StuGrowth::getGrowId, growthItem.getId())
                        .eq(StuGrowth::getStudentId, studentId));
                Integer count = stuGrowth.getCount();
                if (count >= collectLimit) throw new RuntimeException("该学生项目申请已达设置次数，无法再次申请");
            }
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

        // 所有学生id
        List<Long> allStudentIds = studentMapper.selectIdList();
        // 查询对应的项目
        List<GrowthItem> growthItems = growthItemService.list(Wrappers.<GrowthItem>lambdaQuery().eq(GrowthItem::getScorePeriod, periodEnumValue));
        int growthItemSize = growthItems.size();
        for (int i = 0; i < growthItemSize; i++) {
            GrowthItem growthItem = growthItems.get(i);
            Long growthItemId = growthItem.getId();
            Integer calculateType = growthItem.getCalculateType();
            BigDecimal scoreUpperLimit = growthItem.getScoreUpperLimit();  // 项目积分上限
            BigDecimal growthItemScore = growthItem.getScore();                      // 项目单次可获得积分
            if (growthItemScore.compareTo(scoreUpperLimit) > 0) {
                log.error("{}-项目设置不合法", growthItem.getName());
                throw new RuntimeException(StrUtil.format("{}-项目设置不合法", growthItem.getName()));
            }


            // 查询该项目的导入的记录
            List<Long> recDefaults = recDefaultService.listObjs(Wrappers.<RecDefault>lambdaQuery()
                            .select(RecDefault::getStudentId)
                            .eq(RecDefault::getGrowId, growthItemId)
                            .between(RecDefault::getCreateTime, timePeriod.getStartTime(), timePeriod.getEndTime()),
                    id -> Long.valueOf(String.valueOf(id)));

            // 统计每个学生出现的次数
            Map<Long, Integer> studentFrequencyMap = new HashMap<>();
            int recDefaultSize = recDefaults.size();
            for (int j = 0; j < recDefaultSize; j++) {
                Long studentId = recDefaults.get(j);
                Integer count = studentFrequencyMap.get(studentId);
                if (count == null) {
                    studentFrequencyMap.put(studentId, 1);
                } else {
                    studentFrequencyMap.put(studentId, count + 1);
                }
            }
            if (calculateType == CalculateTypeEnum.PLUS.getValue()) {   // 加分
                for (Map.Entry<Long, Integer> entry : studentFrequencyMap.entrySet()) {
                    Long studentId = entry.getKey();
                    Integer count = entry.getValue();    //当前出现次数
                    savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, date, count, growthItemScore, scoreUpperLimit);
                }
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {   // 扣分
                // 对扣分同学的数组进行去重，防止重复添加剩余分
                Set<Long> defaultStudentIdSet = new HashSet<>(recDefaults);
                int size = allStudentIds.size();
                for (int j = 0; j < size; j++) {
                    Long studentId = allStudentIds.get(j);
                    Integer count = studentFrequencyMap.getOrDefault(studentId, 1);
                    if (defaultStudentIdSet.contains(studentId)) {
                        saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, date, count, growthItemScore, scoreUpperLimit);
                    } else {
                        savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, date, count, scoreUpperLimit);
                    }
                }
            } else {
                log.error("项目积分计算类型不存在：”{}“，请检查项目”{}“配置", calculateType, growthItem.getName());
            }
        }
        allStudentIds.clear();
        growthItems.clear();


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

    /**
     * 不限周期 不限次数 加分
     *
     * @param studentId       学生id
     * @param growthItemId    项目id
     * @param yearId          年份id
     * @param semesterId      学期id
     * @param createTime      创建时间
     * @param count           统计次数
     * @param growthItemScore 项目分数
     */
    private void savePlusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore) {
        try {
            calculateScoreService.savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore);
        } catch (Exception e) {
            CalculateFailDTO calculateFailDTO = new CalculateFailDTO();
            calculateFailDTO.setStudentId(studentId);
            calculateFailDTO.setGrowId(growthItemId);
            calculateFailDTO.setYearId(yearId);
            calculateFailDTO.setSemesterId(semesterId);
            calculateFailDTO.setError(e.getMessage());
            calculateFailDTO.setCreateTime(createTime);
            calculateFailDTO.setCount(count);
            calculateFailDTO.setCalculateType(CalculateTypeEnum.PLUS.getValue());
            eventPublish.publishEvent(new CalculateFailEvent(calculateFailDTO));
        } finally {
            stuGrowthService.setCollectCount(studentId, growthItemId, count);
        }
    }

    /**
     * 不限周期 限制次数 加分
     *
     * @param studentId       学生id
     * @param growthItemId    项目id
     * @param yearId          年份id
     * @param semesterId      学期id
     * @param createTime      创建时间
     * @param count           统计次数
     * @param useCount        已使用次数
     * @param countUpperLimit 次数上限
     * @param growthItemScore 项目分数
     */
    private void savePlusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, Integer useCount, Integer countUpperLimit, BigDecimal growthItemScore) {
        int residualCount = countUpperLimit - useCount;
        if (residualCount > 0) {
            int effectiveCount;
            if (count < residualCount) {
                effectiveCount = count;
            } else {
                effectiveCount = residualCount;
            }
            try {
                calculateScoreService.savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, effectiveCount, growthItemScore);
            } catch (Exception e) {
                CalculateFailDTO calculateFailDTO = new CalculateFailDTO();
                calculateFailDTO.setStudentId(studentId);
                calculateFailDTO.setGrowId(growthItemId);
                calculateFailDTO.setYearId(yearId);
                calculateFailDTO.setSemesterId(semesterId);
                calculateFailDTO.setError(e.getMessage());
                calculateFailDTO.setCreateTime(createTime);
                calculateFailDTO.setCount(effectiveCount);
                calculateFailDTO.setCalculateType(CalculateTypeEnum.PLUS.getValue());
                eventPublish.publishEvent(new CalculateFailEvent(calculateFailDTO));
            } finally {
                stuGrowthService.setCollectCount(studentId, growthItemId, effectiveCount);
            }
        }
    }

    /**
     * 限制周期分数上限 加分
     *
     * @param studentId       学生id
     * @param growthItemId    项目id
     * @param yearId          年份id
     * @param semesterId      学期id
     * @param createTime      创建时间
     * @param count           统计次数
     * @param growthItemScore 增加分数
     * @param scoreUpperLimit 分数上限
     */
    private void savePlusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore, BigDecimal scoreUpperLimit) {
        try {
            calculateScoreService.savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore, scoreUpperLimit);
        } catch (Exception e) {
            CalculateFailDTO calculateFailDTO = new CalculateFailDTO();
            calculateFailDTO.setStudentId(studentId);
            calculateFailDTO.setGrowId(growthItemId);
            calculateFailDTO.setYearId(yearId);
            calculateFailDTO.setSemesterId(semesterId);
            calculateFailDTO.setError(e.getMessage());
            calculateFailDTO.setCreateTime(createTime);
            calculateFailDTO.setCount(count);
            calculateFailDTO.setCalculateType(CalculateTypeEnum.PLUS.getValue());
            eventPublish.publishEvent(new CalculateFailEvent(calculateFailDTO));
        } finally {
            stuGrowthService.setCollectCount(studentId, growthItemId, count);
        }
    }

    /**
     * 不限周期 不限次数 扣分
     *
     * @param studentId       学生id
     * @param growthItemId    项目id
     * @param yearId          年份id
     * @param semesterId      学期id
     * @param createTime      创建时间
     * @param count           统计次数
     * @param growthItemScore 扣除分数
     */
    private void saveMinusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore) {
        try {
            calculateScoreService.saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore);
        } catch (Exception e) {
            CalculateFailDTO calculateFailDTO = new CalculateFailDTO();
            calculateFailDTO.setStudentId(studentId);
            calculateFailDTO.setGrowId(growthItemId);
            calculateFailDTO.setYearId(yearId);
            calculateFailDTO.setSemesterId(semesterId);
            calculateFailDTO.setError(e.getMessage());
            calculateFailDTO.setCreateTime(createTime);
            calculateFailDTO.setCount(count);
            calculateFailDTO.setCalculateType(CalculateTypeEnum.MINUS.getValue());
            eventPublish.publishEvent(new CalculateFailEvent(calculateFailDTO));
        } finally {
            stuGrowthService.setCollectCount(studentId, growthItemId, count);
        }
    }

    /**
     * 不限周期 限制次数 扣分
     *
     * @param studentId       学生id
     * @param growthItemId    项目id
     * @param yearId          年份id
     * @param semesterId      学期id
     * @param createTime      创建时间
     * @param count           统计次数
     * @param useCount        已使用次数
     * @param countUpperLimit 次数上限
     * @param growthItemScore 扣除分数
     */
    private void saveMinusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, Integer useCount, Integer countUpperLimit, BigDecimal growthItemScore) {
        int residualCount = countUpperLimit - useCount;
        if (residualCount > 0) {
            int effectiveCount;
            if (count < residualCount) {
                effectiveCount = count;
            } else {
                effectiveCount = residualCount;
            }
            try {
                calculateScoreService.saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, effectiveCount, growthItemScore);
            } catch (Exception e) {
                CalculateFailDTO calculateFailDTO = new CalculateFailDTO();
                calculateFailDTO.setStudentId(studentId);
                calculateFailDTO.setGrowId(growthItemId);
                calculateFailDTO.setYearId(yearId);
                calculateFailDTO.setSemesterId(semesterId);
                calculateFailDTO.setError(e.getMessage());
                calculateFailDTO.setCreateTime(createTime);
                calculateFailDTO.setCount(effectiveCount);
                calculateFailDTO.setCalculateType(CalculateTypeEnum.MINUS.getValue());
                eventPublish.publishEvent(new CalculateFailEvent(calculateFailDTO));
            } finally {

                stuGrowthService.setCollectCount(studentId, growthItemId, effectiveCount);
            }
        }
    }

    /**
     * 限制周期分数上限 扣分
     *
     * @param studentId       学生id
     * @param growthItemId    项目id
     * @param yearId          年份id
     * @param semesterId      学期id
     * @param createTime      创建时间
     * @param count           统计次数
     * @param growthItemScore 扣除分数
     * @param scoreUpperLimit 分数上限
     */
    private void saveMinusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore, BigDecimal scoreUpperLimit) {
        try {
            calculateScoreService.saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore, scoreUpperLimit);
        } catch (Exception e) {
            CalculateFailDTO calculateFailDTO = new CalculateFailDTO();
            calculateFailDTO.setStudentId(studentId);
            calculateFailDTO.setGrowId(growthItemId);
            calculateFailDTO.setYearId(yearId);
            calculateFailDTO.setSemesterId(semesterId);
            calculateFailDTO.setError(e.getMessage());
            calculateFailDTO.setCreateTime(createTime);
            calculateFailDTO.setCount(count);
            calculateFailDTO.setCalculateType(CalculateTypeEnum.MINUS.getValue());
            eventPublish.publishEvent(new CalculateFailEvent(calculateFailDTO));
        } finally {
            stuGrowthService.setCollectCount(studentId, growthItemId, count);
        }
    }

}
