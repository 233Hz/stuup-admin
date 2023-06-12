package com.poho.stuup.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author BUNGA
 * @description: 生成排行榜事件
 * @date 2023/6/12 14:32
 */
public class YearRankingEvent extends ApplicationEvent {

    private final Long yearId;

    public YearRankingEvent(Long yearId) {
        super(yearId);
        this.yearId = yearId;
    }

    public Long getYearId() {
        return yearId;
    }

}
