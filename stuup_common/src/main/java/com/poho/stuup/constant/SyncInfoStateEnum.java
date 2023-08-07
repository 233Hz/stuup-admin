package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum SyncInfoStateEnum {

    SYNC_IN("同步中", 1),
    SYNC_SUCCESS("同步成功", 2),
    SYNC_FAIL("同步失败", 3);


    private final String key;
    private final int value;

    SyncInfoStateEnum(String key, int value) {
        this.key = key;
        this.value = value;
    }

    // 通过value获取枚举
    public static SyncInfoStateEnum getByValue(int value) {
        for (SyncInfoStateEnum periodEnum : SyncInfoStateEnum.values()) {
            if (periodEnum.getValue() == value) {
                return periodEnum;
            }
        }
        return null;
    }


}
