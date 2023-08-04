package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 成长积分统计
 * @date 2023/7/20 14:56
 */

@Data
public class GrowthScoreCountVO {

    /**
     * 成长项目名称
     */
    private String growthName;

    /**
     * 成长项小计
     */
    private int growthItemCount;

    /**
     * 总分
     */
    private BigDecimal totalScore;


    /**
     * 分数变化类型
     */
    private Integer scoreChangeType;

    /**
     * 变化值
     */
    private BigDecimal changeValue;

}
