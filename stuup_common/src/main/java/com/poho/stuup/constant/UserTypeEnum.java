package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 用户类型枚举类
 * @date 2023/6/9 9:57
 */
@Getter
public enum UserTypeEnum {

    STUDENT("学生", 1),
    TEACHER("老师", 2);

    private final String type;
    private final int value;


    UserTypeEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }
}
