package com.poho.stuup.constant;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Getter
public enum PeriodEnum {
    UNLIMITED("不限", 1),
    DAY("每天", 2),
    WEEK("每周", 3),
    MONTH("每月", 4),
    SEMESTER("每学期", 5),
    YEAR("每年", 6),
    THREE_YEAR("三年", 7);

    private final String key;
    private final int value;

    PeriodEnum(String key, int value) {
        this.key = key;
        this.value = value;
    }

    // 通过value获取枚举
    public static PeriodEnum getByValue(int value) {
        for (PeriodEnum periodEnum : PeriodEnum.values()) {
            if (periodEnum.getValue() == value) {
                return periodEnum;
            }
        }
        return null;
    }

    // 获取当天的起止时间
    public LocalDateTime getStartTime(LocalDateTime date, PeriodEnum periodEnum) {
        switch (periodEnum) {
            case DAY:
                return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
            case WEEK:
                return date.with(TemporalAdjusters.previousOrSame(date.getDayOfWeek()))
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case MONTH:
                return date.with(TemporalAdjusters.firstDayOfMonth())
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case SEMESTER:
                return getSemesterStartTime(date);
            case YEAR:
                return date.with(TemporalAdjusters.firstDayOfYear())
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case THREE_YEAR:
                return LocalDateTimeUtil.offset(date, -2, ChronoUnit.YEARS).with(TemporalAdjusters.firstDayOfYear())
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            default:
                return null;
        }
    }

    public LocalDateTime getEndTime(LocalDateTime date, PeriodEnum periodEnum) {
        switch (periodEnum) {
            case DAY:
                return date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case WEEK:
                return date.with(TemporalAdjusters.nextOrSame(date.getDayOfWeek()))
                        .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case MONTH:
                return date.with(TemporalAdjusters.lastDayOfMonth())
                        .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case SEMESTER:
                return getSemesterEndTime(date);
            case YEAR:
                return date.with(TemporalAdjusters.lastDayOfYear())
                        .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case THREE_YEAR:
                return date.with(TemporalAdjusters.lastDayOfYear())
                        .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            default:
                return null;
        }
    }

    private LocalDateTime getSemesterStartTime(LocalDateTime date) {
        int year1 = LocalDateTimeUtil.offset(date, -1, ChronoUnit.YEARS).getYear();
        int year2 = date.getYear();
        int year3 = LocalDateTimeUtil.offset(date, 1, ChronoUnit.YEARS).getYear();
        LocalDateTime time1 = LocalDateTime.of(year1, 9, 1, 0, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(year2, 1, 15, 23, 59, 59);
        LocalDateTime time3 = LocalDateTime.of(year2, 2, 15, 0, 0, 0);
        LocalDateTime time4 = LocalDateTime.of(year2, 6, 30, 23, 59, 59);
        LocalDateTime time5 = LocalDateTime.of(year2, 9, 1, 0, 0, 0);
        LocalDateTime time6 = LocalDateTime.of(year3, 1, 15, 23, 59, 59);
        if (LocalDateTimeUtil.isIn(date, time1, time2, true, true)) {
            return time1;
        }
        if (LocalDateTimeUtil.isIn(date, time3, time4, true, true)) {
            return time3;
        }
        if (LocalDateTimeUtil.isIn(date, time5, time6, true, true)) {
            return time5;
        }
        throw new RuntimeException("不在学期日期范围");
    }

    private LocalDateTime getSemesterEndTime(LocalDateTime date) {
        int year1 = LocalDateTimeUtil.offset(date, -1, ChronoUnit.YEARS).getYear();
        int year2 = date.getYear();
        int year3 = LocalDateTimeUtil.offset(date, 1, ChronoUnit.YEARS).getYear();
        LocalDateTime time1 = LocalDateTime.of(year1, 9, 1, 0, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(year2, 1, 15, 23, 59, 59);
        LocalDateTime time3 = LocalDateTime.of(year2, 2, 15, 0, 0, 0);
        LocalDateTime time4 = LocalDateTime.of(year2, 6, 30, 23, 59, 59);
        LocalDateTime time5 = LocalDateTime.of(year2, 9, 1, 0, 0, 0);
        LocalDateTime time6 = LocalDateTime.of(year3, 1, 15, 23, 59, 59);
        if (LocalDateTimeUtil.isIn(date, time1, time2, true, true)) {
            return time2;
        }
        if (LocalDateTimeUtil.isIn(date, time3, time4, true, true)) {
            return time4;
        }
        if (LocalDateTimeUtil.isIn(date, time5, time6, true, true)) {
            return time6;
        }
        throw new RuntimeException("不在学期日期范围");
    }

}
