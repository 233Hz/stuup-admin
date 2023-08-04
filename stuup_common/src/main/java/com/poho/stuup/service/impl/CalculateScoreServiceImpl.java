package com.poho.stuup.service.impl;

import com.poho.stuup.dao.RecScoreMapper;
import com.poho.stuup.dao.StuScoreLogMapper;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.StuScoreLog;
import com.poho.stuup.service.CalculateScoreService;
import com.poho.stuup.service.StuScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author BUNGA
 * @description: 计算分数实现类
 * @date 2023/7/31 18:10
 */
@Service
public class CalculateScoreServiceImpl implements CalculateScoreService {

    @Resource
    private RecScoreMapper recScoreMapper;

    @Resource
    private StuScoreLogMapper stuScoreLogMapper;

    @Resource
    private StuScoreService stuScoreService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore) {
        BigDecimal totalPlusScore = growthItemScore.multiply(new BigDecimal(count));
        RecScore recScore = new RecScore();
        recScore.setStudentId(studentId);
        recScore.setGrowId(growthItemId);
        recScore.setScore(totalPlusScore);
        recScore.setYearId(yearId);
        recScore.setSemesterId(semesterId);
        recScore.setCreateTime(createTime);
        recScoreMapper.insert(recScore);
        StuScoreLog stuScoreLog = new StuScoreLog();
        stuScoreLog.setStudentId(studentId);
        stuScoreLog.setGrowId(growthItemId);
        stuScoreLog.setScore(totalPlusScore);
        stuScoreLog.setYearId(yearId);
        stuScoreLog.setSemesterId(semesterId);
        stuScoreLog.setCreateTime(createTime);
        stuScoreLogMapper.insert(stuScoreLog);
        stuScoreService.updateTotalScore(studentId, totalPlusScore);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore, BigDecimal scoreUpperLimit) {
        BigDecimal totalPlusScore = growthItemScore.multiply(new BigDecimal(count));
        if (totalPlusScore.compareTo(scoreUpperLimit) > 0) {
            totalPlusScore = scoreUpperLimit;
        }
        RecScore recScore = new RecScore();
        recScore.setStudentId(studentId);
        recScore.setGrowId(growthItemId);
        recScore.setScore(totalPlusScore);
        recScore.setYearId(yearId);
        recScore.setSemesterId(semesterId);
        recScore.setCreateTime(createTime);
        recScoreMapper.insert(recScore);
        StuScoreLog stuScoreLog = new StuScoreLog();
        stuScoreLog.setStudentId(studentId);
        stuScoreLog.setGrowId(growthItemId);
        stuScoreLog.setScore(totalPlusScore);
        stuScoreLog.setYearId(yearId);
        stuScoreLog.setSemesterId(semesterId);
        stuScoreLog.setCreateTime(createTime);
        stuScoreLogMapper.insert(stuScoreLog);
        stuScoreService.updateTotalScore(studentId, totalPlusScore);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMinusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore) {
        BigDecimal totalMinusScore = growthItemScore.multiply(new BigDecimal(count));
        StuScoreLog stuScoreLog = new StuScoreLog();
        stuScoreLog.setStudentId(studentId);
        stuScoreLog.setGrowId(growthItemId);
        stuScoreLog.setScore(totalMinusScore.negate());
        stuScoreLog.setYearId(yearId);
        stuScoreLog.setSemesterId(semesterId);
        stuScoreLog.setCreateTime(createTime);
        stuScoreLogMapper.insert(stuScoreLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMinusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore, BigDecimal scoreUpperLimit) {
        BigDecimal totalMinusScore = growthItemScore.multiply(new BigDecimal(count));
        if (totalMinusScore.compareTo(scoreUpperLimit) < 0) {
            BigDecimal plusScore = scoreUpperLimit.subtract(totalMinusScore);
            RecScore recScore = new RecScore();
            recScore.setStudentId(studentId);
            recScore.setGrowId(growthItemId);
            recScore.setScore(plusScore);
            recScore.setYearId(yearId);
            recScore.setSemesterId(semesterId);
            recScore.setCreateTime(createTime);
            recScoreMapper.insert(recScore);
            StuScoreLog stuScoreLog = new StuScoreLog();
            stuScoreLog.setStudentId(studentId);
            stuScoreLog.setGrowId(growthItemId);
            stuScoreLog.setScore(plusScore);
            stuScoreLog.setYearId(yearId);
            stuScoreLog.setSemesterId(semesterId);
            stuScoreLog.setCreateTime(createTime);
            stuScoreLogMapper.insert(stuScoreLog);
            stuScoreService.updateTotalScore(studentId, plusScore);
        }
        StuScoreLog stuScoreLog = new StuScoreLog();
        stuScoreLog.setStudentId(studentId);
        stuScoreLog.setGrowId(growthItemId);
        stuScoreLog.setScore(totalMinusScore.negate());
        stuScoreLog.setYearId(yearId);
        stuScoreLog.setSemesterId(semesterId);
        stuScoreLog.setCreateTime(createTime);
        stuScoreLogMapper.insert(stuScoreLog);
    }

}
