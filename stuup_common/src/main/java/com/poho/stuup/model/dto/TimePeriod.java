package com.poho.stuup.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 时间段对象
 * @date 2023/5/30 13:51
 */
@Getter
@Setter
@Builder
public class TimePeriod {

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
