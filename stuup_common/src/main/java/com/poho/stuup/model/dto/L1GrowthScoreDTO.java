package com.poho.stuup.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class L1GrowthScoreDTO {

    /**
     * 一级项目id
     */
    private Long l1Id;

    /**
     * 分数
     */
    private BigDecimal score;

}
