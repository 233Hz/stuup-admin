package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 系统配置key
 * @date 2023/8/7 17:21
 */
@Getter
public enum ConfigKeyEnum {
    LAST_SEMESTER_START_TIME("last_semester_start_time", "上学期开始时间"),
    LAST_SEMESTER_END_TIME("last_semester_end_time", "上学期结束时间"),
    NEXT_SEMESTER_START_TIME("next_semester_start_time", "下学期开始时间"),
    NEXT_SEMESTER_END_TIME("next_semester_end_time", "下学期结束时间"),
    PROGRESS_NOTIFY_RANKING("progress_notify_ranking", "进步提醒名次"),
    RETROGRESS_NOTIFY_RANKING("retrogress_notify_ranking", "退步提醒名次"),
    NATIONAL_LEVEL_COMPETITION_AWARD_GROWTH_CODE("national_level_competition_award_growth_code", "国家级竞赛获奖项目code"),
    CITY_LEVEL_COMPETITION_AWARD_GROWTH_CODE("city_level_competition_award_growth_code", "市级竞赛获奖项目code"),
    DISTRICT_LEVEL_COMPETITION_AWARD_GROWTH_CODE("district_level_competition_award_growth_code", "区级竞赛获奖项目code"),
    SCHOOL_LEVEL_COMPETITION_AWARD_GROWTH_CODE("school_level_competition_award_growth_code", "校级竞赛获奖项目code"),
    SCHOLARSHIP_GROWTH_CODE("scholarship_growth_code", "奖学金项目code"),
    HOLD_AN_ACTIVITY_GROWTH_CODE("hold_an_activity_growth_code", "举办活动项目code"),
    GET_CERTIFICATE_GROWTH_CODE("get_certificate_growth_code", "获得证书项目code"),
    SCORE_TIMEOUT_AUTO_COLLECT("score_timeout_auto_collect", "积分超时自动收取时间"),
    GROWTH_TIMER_TASK_SWITCH("growth_timer_task_switch", "成长定时任务开关"),
    ;
    private final String key;
    private final String description;

    ConfigKeyEnum(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
