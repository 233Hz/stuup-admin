package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 进步状态枚举类
 * @date 2023/6/9 9:57
 */
@Getter
public enum ProgressStateEnum {

    UP("上升", 1),
    DOWN("下降", 2),
    SAME("不变", 3);

    private final String type;
    private final int value;


    ProgressStateEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }
}
