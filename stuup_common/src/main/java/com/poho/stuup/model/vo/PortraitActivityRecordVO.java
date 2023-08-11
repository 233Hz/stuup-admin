package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 学生画像-参加活动记录
 * @date 2023/8/7 17:08
 */
@Data
public class PortraitActivityRecordVO {

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 参加时间
     */
    private Date activityTime;
}
