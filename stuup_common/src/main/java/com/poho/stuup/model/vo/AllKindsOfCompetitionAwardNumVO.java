package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 各类竞赛获奖人次可视化对象
 * @date 2023/7/20 9:45
 */

@Data
public class AllKindsOfCompetitionAwardNumVO {

    /**
     * 获奖类型
     */
    private String awardType;

    /**
     * 获奖次数
     */
    private Long awardNum;
}
