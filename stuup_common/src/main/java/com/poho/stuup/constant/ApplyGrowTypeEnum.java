package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 申请项目类型
 * @date 2023/9/5 14:51
 */
@Getter
public enum ApplyGrowTypeEnum {
    SELF("本人申请", 1),
    OTHERS("他人导入", 2);

    private final String type;
    private final int value;

    ApplyGrowTypeEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }
}
