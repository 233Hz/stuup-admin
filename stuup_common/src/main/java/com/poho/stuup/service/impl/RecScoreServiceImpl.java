package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.dao.RecScoreMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.SchoolClaRankDTO;
import com.poho.stuup.model.dto.SchoolStuRankDTO;
import com.poho.stuup.model.dto.TimePeriod;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.SchoolClaRankVO;
import com.poho.stuup.model.vo.SchoolStuRankVO;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.service.RecDefaultService;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private YearMapper yearMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private StuScoreService stuScoreService;

    @Resource
    private RecDefaultService recDefaultService;

    @Resource
    private GrowthItemService growthItemService;

    @Override
    public IPage<RecScoreVO> getRecScorePage(Page<RecScoreVO> page, RecScoreDTO query) {
        return baseMapper.getRecScorePage(page, query);
    }

    @Override
    public void calculateScore(List<RecDefault> recDefaults, Long yearId, GrowthItem growthItem) {
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
     * @description: 计算单个学生的积分
     * @param: recScore
     * @param: studentScore     学生当前积分
     * @param: growScore        项目可获取积分
     * @param: scoreUpperLimit  项目积分上限
     * @return: int
     * @author BUNGA
     * @date: 2023/6/6 14:47
     */
    private int calculateStudentScore(RecScore recScore, int studentScore, int growScore, int scoreUpperLimit) {
        // 溢出积分 = （学生当前项目总积分 + 项目单次可获得积分） - 项目积分上限
        int overflowScore = Math.max((studentScore + growScore) - scoreUpperLimit, 0); //计算溢出分数
        // 有效积分
        int effectiveScore;
        if (overflowScore > 0) {    //分数溢出
            // 有效分数 = 项目可获得分数 - 溢出分数 (为负数返回0)
            effectiveScore = Math.max(growScore - overflowScore, 0);
        } else {    // 分数未溢出
            // 有效分数 = 项目单次可获得积分
            effectiveScore = growScore;
        }
        recScore.setScore(effectiveScore);
        this.save(recScore);

        // 更新学生当前总积分
        if (effectiveScore > 0) {   // 有效分数大于0才更新学生总分
            stuScoreService.updateTotalScore(recScore.getStudentId(), effectiveScore);
        }

        //返回学生当前项目总积分 + 有效积分 为后面更新map的值
        return studentScore + effectiveScore;
    }

    @Override
    public Map<Long, Integer> findTimePeriodScoreMap(Long growthId, Date startTime, Date endTime) {
        List<RecScore> recScores = baseMapper.findTimePeriodRecord(growthId, startTime, endTime);
        return recScores.stream().collect(Collectors.groupingBy(RecScore::getStudentId, Collectors.summingInt(RecScore::getScore)));
    }

    @Override
    public IPage<SchoolStuRankVO> getSchoolStuRank(Page<SchoolStuRankVO> page, SchoolStuRankDTO query) {
        Year year = yearMapper.selectByPrimaryKey(query.getYearId());
        IPage<SchoolStuRankVO> schoolStuRankPage = baseMapper.getSchoolStuRank(page, query);
        List<SchoolStuRankVO> records = schoolStuRankPage.getRecords();
        records.forEach(record -> record.setYearName(year.getYearName()));
        return schoolStuRankPage;
    }

    @Override
    public IPage<SchoolClaRankVO> getSchoolClaRank(Page<SchoolClaRankVO> page, SchoolClaRankDTO query) {
        Year year = yearMapper.selectByPrimaryKey(query.getYearId());
        IPage<SchoolClaRankVO> schoolClaRankPage = baseMapper.getSchoolClaRank(page, query);
        List<SchoolClaRankVO> records = schoolClaRankPage.getRecords();
        records.forEach(record -> record.setYearName(year.getYearName()));
        return schoolClaRankPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateScore(PeriodEnum periodEnum) {
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
            // 所有学生id
            List<Long> allStudentIds = studentMapper.selectIdList();

            Integer calculateType = growthItem.getCalculateType();
            int scoreUpperLimit = growthItem.getScoreUpperLimit();  // 项目积分上限
            int score = growthItem.getScore();                      // 项目单次可获得积分
            if (score > scoreUpperLimit) throw new RuntimeException("该项目设置不合法");

            // 保存学生和对应的积分
            Map<Long, Integer> studentScoreMap = new HashMap<>();

            // 保存学生积分获取记录
            if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
                // 导入为计分的
                defaultStudentIds.forEach(studentId -> {
                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItemId);
                    recScore.setYearId(yearId);

                    int studentScore = studentScoreMap.getOrDefault(studentId, 0); // 学生当前项目总积分
                    // 更新map为 学生当前项目总积分 + 有效积分
                    studentScoreMap.put(studentId, calculateStudentScore(recScore, studentScore, score, scoreUpperLimit));
                });
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
                // 导入为不计分的排除在 defaultStudentIds 里面的学生
                allStudentIds.forEach(studentId -> {
                    if (defaultStudentIds.contains(studentId)) return;

                    RecScore recScore = new RecScore();
                    recScore.setStudentId(studentId);
                    recScore.setGrowId(growthItemId);
                    recScore.setYearId(yearId);

                    int studentScore = studentScoreMap.getOrDefault(studentId, 0); // 学生当前项目总积分
                    // 更新map为 学生当前项目总积分 + 有效积分
                    studentScoreMap.put(studentId, calculateStudentScore(recScore, studentScore, score, scoreUpperLimit));
                });
            } else {
                log.error("项目积分计算类型不存在：”{}“，请检查项目”{}“配置", calculateType, growthItem.getName());
            }
        }
    }

}
