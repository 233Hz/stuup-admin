package com.poho.stuup.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author BUNGA
 * @description: 计算分数接口
 * @date 2023/7/31 18:08
 */
public interface CalculateScoreService {


    void saveAddScoreCalculateResult(Long yearId, Long semesterId, Date time, Long growthItemId, Long studentId, Long jobGrowId, BigDecimal addScore);

    void saveAddScoreCalculateResult(Long yearId, Long semesterId, Date time, Long growthItemId, Long studentId, Long jobGrowId, long count, BigDecimal growthItemScore, BigDecimal growthItemScoreUpperLimit);

    void saveDeductScoreCalculateResult(Long yearId, Long semesterId, Date time, Long growthItemId, Long studentId, Long jobGrowId, long count, BigDecimal growthItemScore, BigDecimal growthItemScoreUpperLimit);
}
