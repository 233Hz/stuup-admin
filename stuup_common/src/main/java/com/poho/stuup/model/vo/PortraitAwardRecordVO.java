package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 学生画像-获奖记录
 * @date 2023/8/7 17:08
 */
@Data
public class PortraitAwardRecordVO {

    /**
     * 获奖名称
     */
    private String awardName;

    /**
     * 获奖类型
     */
    private Integer awardType;

    /**
     * 获奖时间
     */
    private Date awardTime;
}
