package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 审核状态枚举类
 */
@Getter
public enum AutStateEnum {
    TO_BE_SUBMITTED("待提交", 1),
    PENDING_REVIEW("待审核", 2),
    PASS("通过", 3),
    REFUSE("拒绝", 4),
    RETURN("退回", 5);

    private final String state;
    private final int code;

    AutStateEnum(String state, int code) {
        this.state = state;
        this.code = code;
    }
}
