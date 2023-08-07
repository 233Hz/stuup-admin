package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum SyncCommunityMemberStateEnum {

    HANDLER_WAIT("未处理", 0),
    HANDLER_SUCCESS("处理成功", 1),
    HANDLER_FAIL("处理失败", 2);


    private final String key;
    private final int value;

    SyncCommunityMemberStateEnum(String key, int value) {
        this.key = key;
        this.value = value;
    }

    // 通过value获取枚举
    public static SyncCommunityMemberStateEnum getByValue(int value) {
        for (SyncCommunityMemberStateEnum periodEnum : SyncCommunityMemberStateEnum.values()) {
            if (periodEnum.getValue() == value) {
                return periodEnum;
            }
        }
        return null;
    }


}
