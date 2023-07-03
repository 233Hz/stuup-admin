package com.poho.stuup.event;

import com.poho.stuup.model.dto.StatisticsRankEventDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author BUNGA
 * @description: 统计学年排行榜事件
 * @date 2023/6/12 14:32
 */
public class StatisticsYearRankEvent extends ApplicationEvent {

    @Getter
    private final StatisticsRankEventDTO statisticsRankEvent;

    public StatisticsYearRankEvent(StatisticsRankEventDTO statisticsRankEvent) {
        super(statisticsRankEvent);
        this.statisticsRankEvent = statisticsRankEvent;
    }

}
