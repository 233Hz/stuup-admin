package com.poho.stuup.event;

import com.poho.stuup.model.dto.StatisticsRankEventDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author BUNGA
 * @description: 统计每月排行榜事件
 * @date 2023/6/12 14:32
 */
public class StatisticsMonthRankEvent extends ApplicationEvent {

    @Getter
    private final StatisticsRankEventDTO statisticsRankEvent;

    public StatisticsMonthRankEvent(StatisticsRankEventDTO statisticsRankEvent) {
        super(statisticsRankEvent);
        this.statisticsRankEvent = statisticsRankEvent;
    }
}
