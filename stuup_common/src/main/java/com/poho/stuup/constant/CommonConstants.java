package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 公共常量
 * @date 2023/6/28 13:49
 */
public class CommonConstants {

    /**
     * 默认通知名次
     */
    public final static int DEFAULT_NOTIFY_RANKING = 10;

    /**
     * 系统配置key
     */
    @Getter
    public enum ConfigKey {
        LAST_SEMESTER_START_TIME("last_semester_start_time", "上学期开始时间"),
        LAST_SEMESTER_END_TIME("last_semester_end_time", "上学期结束时间"),
        NEXT_SEMESTER_START_TIME("next_semester_start_time", "下学期开始时间"),
        NEXT_SEMESTER_END_TIME("next_semester_end_time", "下学期结束时间"),
        PROGRESS_NOTIFY_RANKING("progress_notify_ranking", "进步提醒名次"),
        RETROGRESS_NOTIFY_RANKING("retrogress_notify_ranking", "退步提醒名次"),
        ;
        private final String key;
        private final String description;

        ConfigKey(String key, String description) {
            this.key = key;
            this.description = description;
        }
    }
}
