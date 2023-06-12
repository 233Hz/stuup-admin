package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 花园类型枚举类
 * @date 2023/6/9 9:57
 */
@Getter
public enum GardenTypeEnum {

    BMH("白梅花园", 1),
    XCJ("雏菊花园", 2),
    XHH("西红花园", 3);

    private final String type;
    private final int value;


    GardenTypeEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }
}
