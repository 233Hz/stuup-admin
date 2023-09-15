package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @date 2023/8/7 19:05
 */

@Data
public class PortraitRankingCurveVO {

    /**
     * 学期名称
     */
    private String semesterName;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 分数
     */
    private BigDecimal score;
}
