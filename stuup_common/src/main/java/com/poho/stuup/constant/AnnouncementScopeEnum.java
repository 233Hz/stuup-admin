package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: 公告范围枚举
 * @date 2023/6/8 17:31
 */
@Getter
public enum AnnouncementScopeEnum {

    ALL("全体用户", 1),
    ALL_TEACHER("全体教师", 2);

    private final String scope;
    private final int value;

    AnnouncementScopeEnum(String scope, int value) {
        this.scope = scope;
        this.value = value;
    }

}
