package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 学生画像-成长能力
 * @date 2023/8/7 16:07
 */
@Data
public class PortraitCapacityEvaluatorVO {

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 指标值
     */
    private BigDecimal indicatorScore;

    /**
     * 指标平均值
     */
    private BigDecimal indicatorAvgScore;
}
