package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 公告发布状态枚举
 */
@Getter
public enum AnnouncementStateEnum {

    PUBLISHED("已发布", 1),
    UNPUBLISHED("未发布", 2);

    private final String state;
    private final int value;

    AnnouncementStateEnum(String state, int value) {
        this.state = state;
        this.value = value;
    }
}
