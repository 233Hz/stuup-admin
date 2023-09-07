package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 学生画像-成长对比
 * @date 2023/8/8 9:40
 */
@Data
public class PortraitGrowthComparisonVO {

    /**
     * 成长项名称
     */
    private String growthName;

    /**
     * 我的积分
     */
    private BigDecimal myScore;

    /**
     * 平均积分
     */
    private BigDecimal avgScore;

}
