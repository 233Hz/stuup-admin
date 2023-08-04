package com.poho.stuup.handle.calculationScoreHandle;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 积分计算规则枚举类
 * @date 2023/8/3 16:38
 */
@Getter
public enum CalculateRuleEnum {

    PLUS_ANYTIME(1),
    PLUS_FIXED_NUM(2),
    PLUS_PERIOD_LIMIT(3),
    MINUS_ANYTIME(4),
    MINUS_FIXED_NUM(5),
    MINUS_PERIOD_LIMIT(6),
    ;

    final private int value;

    CalculateRuleEnum(int value) {
        this.value = value;
    }
}
