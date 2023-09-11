package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GrowthRuleDescVO {

    /**
     * 项目id
     */
    private Long id;

    /**
     * 一级项目id
     */
    private Long l1Id;

    /**
     * 二级项目id
     */
    private Long l2Id;

    /**
     * 三级项目id
     */
    private Long l3Id;

    /**
     * 一级项目名称
     */
    private String l1;

    /**
     * 二级项目名称
     */
    private String l2;

    /**
     * 三级项目名称
     */
    private String l3;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 采集周期
     */
    private Integer cycle;

    /**
     * 采集周期内分数上限
     */
    private BigDecimal cycleUpperLimit;

    /**
     * 项目积分
     */
    private BigDecimal score;

    /**
     * 计算类型
     */
    private Integer calculateType;

}
