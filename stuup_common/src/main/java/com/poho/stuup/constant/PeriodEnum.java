package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum PeriodEnum {
    UNLIMITED("不限", 1),
    DAY("每天", 2),
    WEEK("每周", 3),
    MONTH("每月", 4),
    SEMESTER("每学期", 5),
    YEAR("每年", 6);

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
}
