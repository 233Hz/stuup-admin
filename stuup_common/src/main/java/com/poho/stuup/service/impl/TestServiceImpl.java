package com.poho.stuup.service.impl;

import com.poho.stuup.model.RecAddScore;
import com.poho.stuup.model.RecDeductScore;
import com.poho.stuup.model.StuScoreLog;
import com.poho.stuup.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {


    private final RecAddScoreService recAddScoreService;

    private final RecDeductScoreService recDeductScoreService;

    private final StuScoreLogService stuScoreLogService;

    private final StuScoreService stuScoreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, BigDecimal score) {
        RecAddScore recAddScore = new RecAddScore();
        recAddScore.setStudentId(studentId);
        recAddScore.setGrowId(growthItemId);
        recAddScore.setYearId(yearId);
        recAddScore.setSemesterId(semesterId);
        recAddScore.setScore(score);
        recAddScoreService.save(recAddScore);
        StuScoreLog stuScoreLog = new StuScoreLog();
        stuScoreLog.setStudentId(studentId);
        stuScoreLog.setGrowId(growthItemId);
        stuScoreLog.setYearId(yearId);
        stuScoreLog.setSemesterId(semesterId);
        stuScoreLog.setScore(score);
        stuScoreLogService.save(stuScoreLog);
        stuScoreService.updateTotalScore(studentId, score);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, BigDecimal score, BigDecimal scoreUpperLimit, Integer count) {
        for (int i = 0; i < count; i++) {
            RecDeductScore recDeductScore = new RecDeductScore();
            recDeductScore.setStudentId(studentId);
            recDeductScore.setGrowId(growthItemId);
            recDeductScore.setYearId(yearId);
            recDeductScore.setSemesterId(semesterId);
            recDeductScore.setScore(score);
            recDeductScoreService.save(recDeductScore);
            StuScoreLog stuScoreLog = new StuScoreLog();
            stuScoreLog.setStudentId(studentId);
            stuScoreLog.setGrowId(growthItemId);
            stuScoreLog.setYearId(yearId);
            stuScoreLog.setSemesterId(semesterId);
            stuScoreLog.setScore(score.negate());
            stuScoreLogService.save(stuScoreLog);
        }
        BigDecimal totalDeductScore = score.multiply(new BigDecimal(count));
        BigDecimal plusScore = scoreUpperLimit.subtract(totalDeductScore);
        if (plusScore.compareTo(BigDecimal.ZERO) > 0) {
            RecAddScore recAddScore = new RecAddScore();
            recAddScore.setStudentId(studentId);
            recAddScore.setGrowId(growthItemId);
            recAddScore.setYearId(yearId);
            recAddScore.setSemesterId(semesterId);
            recAddScore.setScore(plusScore);
            recAddScoreService.save(recAddScore);
            StuScoreLog stuScoreLog = new StuScoreLog();
            stuScoreLog.setStudentId(studentId);
            stuScoreLog.setGrowId(growthItemId);
            stuScoreLog.setYearId(yearId);
            stuScoreLog.setSemesterId(semesterId);
            stuScoreLog.setScore(plusScore);
            stuScoreLogService.save(stuScoreLog);
            stuScoreService.updateTotalScore(studentId, plusScore);
        }
    }
}
