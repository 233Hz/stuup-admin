package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 成长项目采集者类型枚举类
 */
@Getter
public enum GrowGathererEnum {
    ASSIGN("指定用户", 1),
    STUDENT("学生", 2);

    private final String type;

    private final int value;

    GrowGathererEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }
}
