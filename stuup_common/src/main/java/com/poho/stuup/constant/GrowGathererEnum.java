package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 成长项目采集者类型枚举类
 */
@Getter
public enum GrowGathererEnum {
    TEACHER("老师", "teacher", 1),
    STUDENT_UNION("学生会", "studentUnion", 2),
    STUDENT("学生", "student", 3);

    private final String type;
    private final String code;

    private final int value;

    GrowGathererEnum(String type, String code, int value) {
        this.type = type;
        this.code = code;
        this.value = value;
    }

    public static GrowGathererEnum getEnumByCode(String code) {
        for (GrowGathererEnum e : GrowGathererEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
