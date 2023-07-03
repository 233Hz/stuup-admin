package com.poho.stuup.event;

import com.poho.stuup.model.dto.StatisticsRankEventDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author BUNGA
 * @description: 统计学期排行榜时间
 * @date 2023/6/12 14:32
 */
public class StatisticsSemesterRankEvent extends ApplicationEvent {

    @Getter
    private final StatisticsRankEventDTO statisticsRankEvent;

    public StatisticsSemesterRankEvent(StatisticsRankEventDTO statisticsRankEvent) {
        super(statisticsRankEvent);
        this.statisticsRankEvent = statisticsRankEvent;
    }
}
