package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 学生画像-成长数据
 * @date 2023/8/8 9:40
 */
@Data
public class PortraitGrowthDataVO {

    /**
     * 成长值
     */
    private BigDecimal score;

    /**
     * 全校排名
     */
    private Integer rank;

    /**
     * 扣除分
     */
    private BigDecimal minusScore;

    /**
     * 参加活动次数
     */
    private Long activityCount;

    /**
     * 获奖次数
     */
    private Long awardCount;

    /**
     * 获得证书数
     */
    private Long certificateCount;
}
