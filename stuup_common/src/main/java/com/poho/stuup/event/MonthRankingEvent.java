package com.poho.stuup.event;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 生成排行榜事件
 * @date 2023/6/12 14:32
 */
public class MonthRankingEvent extends ApplicationEvent {

    private final Date now;

    public MonthRankingEvent(Date now) {
        super(now);
        this.now = now;
    }

    public Date getNow() {
        return now;
    }
}
