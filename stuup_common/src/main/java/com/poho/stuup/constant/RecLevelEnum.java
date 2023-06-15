package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum RecLevelEnum {
    SCHOOL("校级", 1),
    DISTRICT("区（行级）", 2),
    CITY("市级", 3),
    COUNTRY("国家", 4),
    INTERNATIONAL("国际", 5);

    private final String label;
    private final int value;

    RecLevelEnum(String label, int value) {
        this.label = label;
        this.value = value;
    }

    //根据label获取对应的枚举值
    public static Integer getValueForLabel(String label, RecLevelEnum... args) {
        // TODO 通过 args 排除参与的枚举
        for (RecLevelEnum recLevelEnum : RecLevelEnum.values()) {
            if (recLevelEnum.getLabel().equals(label)) {
                return recLevelEnum.getValue();
            }
        }
        return null;
    }
}
