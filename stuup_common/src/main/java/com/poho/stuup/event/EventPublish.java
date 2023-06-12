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
        if (event instanceof YearRankingEvent) {
            applicationContext.publishEvent((YearRankingEvent) event);
        } else if (event instanceof MonthRankingEvent) {
            applicationContext.publishEvent((MonthRankingEvent) event);
        } else {
            //发布失败
            log.error("事件发布失败");
        }
    }

    public void yearRankingEventPublish(YearRankingEvent event) {
        applicationContext.publishEvent(event);
    }

    public void monthRankingEventPublish(MonthRankingEvent event) {
        applicationContext.publishEvent(event);
    }
}
