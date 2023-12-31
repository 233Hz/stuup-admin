package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum MilitaryLevelEnum {

    QUALIFIED("合格", 1),
    UNQUALIFIED("不合格", 2);


    private final String label;
    private final int value;

    MilitaryLevelEnum(String label, int value) {
        this.label = label;
        this.value = value;
    }

    //根据label获取对应的枚举值
    public static Integer getValueForLabel(String label) {
        for (MilitaryLevelEnum militaryLevelEnum : MilitaryLevelEnum.values()) {
            if (militaryLevelEnum.getLabel().equals(label)) {
                return militaryLevelEnum.getValue();
            }
        }
        return null;
    }
}
