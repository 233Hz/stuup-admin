package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 成长项目采集者类型枚举类
 */
@Getter
public enum GrowGathererEnum {
    TEACHER("老师", 1),
    STUDENT_UNION("学生会", 2),
    STUDENT("学生", 3);

    private final String type;

    private final int value;

    GrowGathererEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }
}
