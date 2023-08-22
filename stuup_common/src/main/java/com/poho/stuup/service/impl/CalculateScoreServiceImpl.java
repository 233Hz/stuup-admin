package com.poho.stuup.service.impl;

import com.poho.stuup.dao.JobGrowStudentMapper;
import com.poho.stuup.dao.RecAddScoreMapper;
import com.poho.stuup.dao.RecDeductScoreMapper;
import com.poho.stuup.dao.StuScoreLogMapper;
import com.poho.stuup.model.JobGrowStudent;
import com.poho.stuup.model.RecAddScore;
import com.poho.stuup.model.RecDeductScore;
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
    private RecAddScoreMapper recAddScoreMapper;

    @Resource
    private RecDeductScoreMapper recDeductScoreMapper;

    @Resource
    private StuScoreLogMapper stuScoreLogMapper;

    @Resource
    private StuScoreService stuScoreService;

    @Resource
    private JobGrowStudentMapper jobGrowStudentMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAddScoreCalculateResult(Long yearId, Long semesterId, Date time, Long growthItemId, Long studentId, Long jobGrowId, BigDecimal addScore) {
        RecAddScore recAddScore = new RecAddScore();
        recAddScore.setYearId(yearId);
        recAddScore.setSemesterId(semesterId);
        recAddScore.setStudentId(studentId);
        recAddScore.setGrowId(growthItemId);
        recAddScore.setScore(addScore);
        recAddScore.setCreateTime(time);
        recAddScoreMapper.insert(recAddScore);
        StuScoreLog stuScoreLog = new StuScoreLog();
        stuScoreLog.setYearId(yearId);
        stuScoreLog.setSemesterId(semesterId);
        stuScoreLog.setStudentId(studentId);
        stuScoreLog.setGrowId(growthItemId);
        stuScoreLog.setScore(addScore);
        stuScoreLog.setCreateTime(time);
        stuScoreLogMapper.insert(stuScoreLog);
        stuScoreService.updateTotalScore(studentId, addScore);
        JobGrowStudent jobGrowStudent = new JobGrowStudent();
        jobGrowStudent.setJobGrowId(jobGrowId);
        jobGrowStudent.setStudentId(studentId);
        jobGrowStudentMapper.insert(jobGrowStudent);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAddScoreCalculateResult(Long yearId, Long semesterId, Date time, Long growthItemId, Long studentId, Long jobGrowId, long count, BigDecimal growthItemScore, BigDecimal growthItemScoreUpperLimit) {
        for (int i = 1; i <= count; i++) {
            BigDecimal totalScore = growthItemScore.multiply(new BigDecimal(i));
            BigDecimal addScore;
            if (growthItemScoreUpperLimit.subtract(totalScore).compareTo(BigDecimal.ZERO) >= 0) {
                addScore = growthItemScore;
            } else {
                if (totalScore.subtract(growthItemScoreUpperLimit).compareTo(growthItemScore) < 0) {
                    addScore = growthItemScoreUpperLimit.subtract(growthItemScore.multiply(new BigDecimal(i - 1)));
                } else {
                    continue;
                }
            }
            RecAddScore recAddScore = new RecAddScore();
            recAddScore.setYearId(yearId);
            recAddScore.setSemesterId(semesterId);
            recAddScore.setStudentId(studentId);
            recAddScore.setGrowId(growthItemId);
            recAddScore.setScore(addScore);
            recAddScore.setCreateTime(time);
            recAddScoreMapper.insert(recAddScore);
            StuScoreLog stuScoreLog = new StuScoreLog();
            stuScoreLog.setYearId(yearId);
            stuScoreLog.setSemesterId(semesterId);
            stuScoreLog.setStudentId(studentId);
            stuScoreLog.setGrowId(growthItemId);
            stuScoreLog.setScore(addScore);
            stuScoreLog.setCreateTime(time);
            stuScoreLogMapper.insert(stuScoreLog);
            stuScoreService.updateTotalScore(studentId, addScore);
        }

        JobGrowStudent jobGrowStudent = new JobGrowStudent();
        jobGrowStudent.setJobGrowId(jobGrowId);
        jobGrowStudent.setStudentId(studentId);
        jobGrowStudentMapper.insert(jobGrowStudent);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDeductScoreCalculateResult(Long yearId, Long semesterId, Date time, Long growthItemId, Long studentId, Long jobGrowId, long count, BigDecimal growthItemScore, BigDecimal growthItemScoreUpperLimit) {
        for (int i = 0; i < count; i++) {
            RecDeductScore recDeductScore = new RecDeductScore();
            recDeductScore.setYearId(yearId);
            recDeductScore.setSemesterId(semesterId);
            recDeductScore.setStudentId(studentId);
            recDeductScore.setGrowId(growthItemId);
            recDeductScore.setScore(growthItemScore);
            recDeductScore.setCreateTime(time);
            recDeductScoreMapper.insert(recDeductScore);
            StuScoreLog stuScoreLog = new StuScoreLog();
            stuScoreLog.setYearId(yearId);
            stuScoreLog.setSemesterId(semesterId);
            stuScoreLog.setStudentId(studentId);
            stuScoreLog.setGrowId(growthItemId);
            stuScoreLog.setScore(growthItemScore.negate());
            stuScoreLog.setCreateTime(time);
            stuScoreLogMapper.insert(stuScoreLog);
        }
        BigDecimal deductScore = growthItemScore.multiply(BigDecimal.valueOf(count));
        if (deductScore.compareTo(growthItemScoreUpperLimit) < 0) {
            BigDecimal addScore = growthItemScoreUpperLimit.subtract(deductScore);
            RecAddScore recAddScore = new RecAddScore();
            recAddScore.setYearId(yearId);
            recAddScore.setSemesterId(semesterId);
            recAddScore.setStudentId(studentId);
            recAddScore.setGrowId(growthItemId);
            recAddScore.setScore(addScore);
            recAddScore.setCreateTime(time);
            recAddScoreMapper.insert(recAddScore);
            StuScoreLog stuScoreLog = new StuScoreLog();
            stuScoreLog.setYearId(yearId);
            stuScoreLog.setSemesterId(semesterId);
            stuScoreLog.setStudentId(studentId);
            stuScoreLog.setGrowId(growthItemId);
            stuScoreLog.setScore(addScore);
            stuScoreLog.setCreateTime(time);
            stuScoreLogMapper.insert(stuScoreLog);
            stuScoreService.updateTotalScore(studentId, addScore);
        }
        JobGrowStudent jobGrowStudent = new JobGrowStudent();
        jobGrowStudent.setJobGrowId(jobGrowId);
        jobGrowStudent.setStudentId(studentId);
        jobGrowStudentMapper.insert(jobGrowStudent);
    }
}
