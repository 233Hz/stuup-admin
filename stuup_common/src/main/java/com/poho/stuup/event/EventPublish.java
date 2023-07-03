package com.poho.stuup.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author BUNGA
 * @description: 事件发布
 * @date 2023/6/12 14:38
 */
@Slf4j
@Component
public class EventPublish {

    @Resource
    private ApplicationContext applicationContext;

    public void publishEvent(ApplicationEvent event) {
        if (event instanceof StatisticsYearRankEvent) {
            applicationContext.publishEvent(event);
        } else if (event instanceof StatisticsMonthRankEvent) {
            applicationContext.publishEvent(event);
        } else if (event instanceof StatisticsSemesterRankEvent) {
            applicationContext.publishEvent(event);
        } else if (event instanceof SystemMsgEvent) {
            applicationContext.publishEvent(event);
        } else {
            //发布失败
            log.error("事件发布失败");
        }
    }
}
