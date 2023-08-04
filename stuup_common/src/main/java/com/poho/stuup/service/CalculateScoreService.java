package com.poho.stuup.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author BUNGA
 * @description: 计算分数接口
 * @date 2023/7/31 18:08
 */
public interface CalculateScoreService {

    void savePlusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore);

    void savePlusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore, BigDecimal scoreUpperLimit);

    void saveMinusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore);

    void saveMinusCalculateScoreResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, Date createTime, Integer count, BigDecimal growthItemScore, BigDecimal scoreUpperLimit);


}
