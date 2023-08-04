package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.CalculateFailLogMapper;
import com.poho.stuup.model.CalculateFailLog;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.service.CalculateFailLogService;
import com.poho.stuup.service.CalculateScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 积分计算失败记录 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-03
 */

@Service
public class CalculateFailLogServiceImpl extends ServiceImpl<CalculateFailLogMapper, CalculateFailLog> implements CalculateFailLogService {

    @Resource
    private CalculateScoreService calculateScoreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCalculateResult(CalculateFailLog calculateFailLog, GrowthItem growthItem) {
        Long studentId = calculateFailLog.getStudentId();
        Long growthItemId = calculateFailLog.getGrowId();
        Long yearId = calculateFailLog.getYearId();
        Long semesterId = calculateFailLog.getSemesterId();
        Date createTime = calculateFailLog.getCreateTime();
        Integer count = calculateFailLog.getCount();
        Integer calculateType = calculateFailLog.getCalculateType();

        Integer scorePeriod = growthItem.getScorePeriod();
        BigDecimal growthItemScore = growthItem.getScore();
        BigDecimal scoreUpperLimit = growthItem.getScoreUpperLimit();

        boolean isUnlimitedPeriod = scorePeriod == PeriodEnum.UNLIMITED.getValue();
        boolean isPlusCalculate = calculateType == CalculateTypeEnum.PLUS.getValue();

        if (isUnlimitedPeriod) {
            if (isPlusCalculate) {
                calculateScoreService.savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore);
            } else {
                calculateScoreService.saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore);
            }
        } else {
            if (isPlusCalculate) {
                calculateScoreService.savePlusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore, scoreUpperLimit);
            } else {
                calculateScoreService.saveMinusCalculateScoreResult(studentId, growthItemId, yearId, semesterId, createTime, count, growthItemScore, scoreUpperLimit);
            }
        }

        this.update(Wrappers.<CalculateFailLog>lambdaUpdate()
                .set(CalculateFailLog::getStatus, WhetherEnum.YES.getValue())
                .eq(CalculateFailLog::getId, calculateFailLog.getId()));
    }
}
