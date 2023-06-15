package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 成长项类型枚举
 * @date 2023/6/14 10:22
 */

@Getter
public enum GrowItemTypeEnum {
    CUSTOM("自定义", 1),
    SYSTEM("系统类", 2);

    private final String type;
    private final int code;

    GrowItemTypeEnum(String type, int code) {
        this.type = type;
        this.code = code;
    }
}
