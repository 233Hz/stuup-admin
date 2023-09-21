package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortraitCapacityEvaluatorVO {

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 最高分
     */
    private BigDecimal highestScore;

    /**
     * 我的分数
     */
    private BigDecimal myScore;

    /**
     * 平均分
     */
    private BigDecimal avgScore;

}
