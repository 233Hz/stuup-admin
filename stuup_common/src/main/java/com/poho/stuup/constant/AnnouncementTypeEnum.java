package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 公告类型枚举
 */
@Getter
public enum AnnouncementTypeEnum {

    SYSTEM("系统通知", 1),
    ANNOUNCEMENT("通知公告", 2);

    private final String type;
    private final int value;

    AnnouncementTypeEnum(String type, int value) {
        this.type = type;
        this.value = value;
    }
}
