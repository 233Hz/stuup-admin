package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/5/29 13:16
 */
@Getter
public enum CalculateTypeEnum {

    PLUS("+", 1),
    MINUS("-", 2);

    private final String type;
    private final int value;

    CalculateTypeEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }

    // 通过 value 获取 枚举
    public static CalculateTypeEnum getByValue(int value) {
        for (CalculateTypeEnum typeEnum : CalculateTypeEnum.values()) {
            if (typeEnum.value == value) {
                return typeEnum;
            }
        }
        return null;
    }
}
