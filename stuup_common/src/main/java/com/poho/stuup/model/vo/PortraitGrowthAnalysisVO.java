package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 学生画像-成长分析
 * @date 2023/8/8 9:40
 */
@Data
public class PortraitGrowthAnalysisVO {

    /**
     * 学期名称
     */
    private String semesterName;

    /**
     * 与平均分的差距
     */
    private BigDecimal avgScoreGap;
}
