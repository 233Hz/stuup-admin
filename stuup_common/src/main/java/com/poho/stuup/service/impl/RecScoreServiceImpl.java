package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.dao.RecScoreMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.TimePeriod;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
@Service
public class RecScoreServiceImpl extends ServiceImpl<RecScoreMapper, RecScore> implements RecScoreService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private StuScoreService stuScoreService;

    @Override
    public IPage<RecScoreVO> getRecScorePage(Page<RecScoreVO> page, RecScoreDTO query) {
        return baseMapper.getRecScorePage(page, query);
    }

    @Override
    public void calculateScore(List<Long> studentIds, GrowthItem growthItem) {
        Year currYear = yearMapper.findCurrYear();
        List<Long> allStudentIds = studentMapper.selectIdList();
        Integer calculateType = growthItem.getCalculateType();
        List<Long> recordStudentIds;
        if (CalculateTypeEnum.MINUS.getValue() == calculateType) {
            recordStudentIds = allStudentIds.stream().filter(studentId -> !studentIds.contains(studentId)).collect(Collectors.toList());
        } else {
            recordStudentIds = studentIds;
        }
        // 获取项目积分刷新周期
        Integer scorePeriod = growthItem.getScorePeriod();    // 积分刷新周期
        if (PeriodEnum.UNLIMITED.getValue() != scorePeriod) {
            int scoreUpperLimit = growthItem.getScoreUpperLimit();  // 项目积分上限
            int score = growthItem.getScore();                  // 项目单次可获得积分
            PeriodEnum periodEnum = PeriodEnum.getByValue(scorePeriod);
            if (periodEnum == null) throw new RuntimeException("项目时间段设置错误，请联系管理员");
            // 计算周期时间段
            TimePeriod timePeriod = Utils.getCurrentTimePeriod(periodEnum);
            // 查询项目时间段内的记录
            Map<Long, Integer> studentScoreMap = findTimePeriodScoreMap(growthItem.getId(), timePeriod.getStartTime(), timePeriod.getEndTime());
            // 保存学生积分获取记录
            recordStudentIds.forEach(studentId -> {
                // 获取不到默认值为0
                int currentScore = studentScoreMap.getOrDefault(studentId, 0);  // 学生当前项目总积分
                RecScore recScore = new RecScore();
                recScore.setStudentId(studentId);
                recScore.setGrowId(growthItem.getId());
                recScore.setScore(score);
                recScore.setYearId(currYear.getOid());
                // 溢出积分 = （学生当前项目总积分 + 项目单次可获得积分） - 项目积分上限
                int overflowScore = Math.max((currentScore + score) - scoreUpperLimit, 0); //计算溢出分数
                // 有效积分
                int effectiveScore;
                if (overflowScore > 0) {    //分数溢出
                    // 有效分数 = 项目可获得分数 - 溢出分数 (为负数返回0)
                    effectiveScore = Math.max(score - overflowScore, 0);
                } else {    // 分数未溢出
                    // 有效分数 = 项目单次可获得积分
                    effectiveScore = score;
                }
                recScore.setScore(effectiveScore);
                this.save(recScore);

                // 更新map为 学生当前项目总积分 + 有效积分
                studentScoreMap.put(studentId, currentScore + effectiveScore);

                // 更新学生当前总积分
                if (effectiveScore > 0) {   // 有效分数大于0才更新学生总分
                    stuScoreService.updateTotalScore(studentId, effectiveScore);
                }
            });
        } else {
            recordStudentIds.forEach(studentId -> {
                RecScore recScore = new RecScore();
                recScore.setStudentId(studentId);
                recScore.setGrowId(growthItem.getId());
                recScore.setScore(growthItem.getScore());
                recScore.setYearId(currYear.getOid());
                this.save(recScore);
                stuScoreService.updateTotalScore(studentId, growthItem.getScore());
            });
        }
    }

    @Override
    public Map<Long, Integer> findTimePeriodScoreMap(Long growthId, Date startTime, Date endTime) {
        List<RecScore> recScores = baseMapper.findTimePeriodRecord(growthId, startTime, endTime);
        return recScores.stream().collect(Collectors.groupingBy(RecScore::getStudentId, Collectors.summingInt(RecScore::getScore)));
    }


}
