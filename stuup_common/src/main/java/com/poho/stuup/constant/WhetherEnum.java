package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum WhetherEnum {

    YES("是", 1),
    NO("否", 2);


    private final String label;
    private final int value;

    WhetherEnum(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public static String getLabelForValue(int value) {
        for (WhetherEnum whetherEnum : WhetherEnum.values()) {
            if (whetherEnum.getValue() == value) {
                return whetherEnum.getLabel();
            }
        }
        return null;
    }

    //根据label获取对应的枚举值
    public static Integer getValueForLabel(String label) {
        for (WhetherEnum whetherEnum : WhetherEnum.values()) {
            if (whetherEnum.getLabel().equals(label)) {
                return whetherEnum.getValue();
            }
        }
        return null;
    }
}
