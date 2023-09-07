package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 审核状态枚举类
 */
@Getter
public enum AudStateEnum {
    TO_BE_SUBMITTED("待提交", 1),
    PENDING_REVIEW("待审核", 2),
    PASS("审核通过", 3),
    NO_PASS("审核不通过", 4);

    private final String state;
    private final int code;

    AudStateEnum(String state, int code) {
        this.state = state;
        this.code = code;
    }
}
