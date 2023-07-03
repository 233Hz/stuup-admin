package com.poho.stuup.model.dto;

import com.poho.stuup.constant.PeriodEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 统计排行榜事件对象
 * @date 2023/6/30 10:43
 */
@Data
public class StatisticsRankEventDTO {

    /**
     * 统计周期
     */
    private PeriodEnum periodEnum;

    /**
     * 统计开始时间
     */
    private Date startTime;

    /**
     * 统计的学年id
     */
    private Long yearId;

    /**
     * 统计的学期id
     */
    private Long semesterId;

}
