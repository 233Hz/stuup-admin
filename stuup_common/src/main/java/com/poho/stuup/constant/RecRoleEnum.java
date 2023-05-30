package com.poho.stuup.constant;

import lombok.Getter;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/5/25 14:34
 */
@Getter
public enum RecRoleEnum {

    PARTICIPANT("参与者", 1),
    PRESIDES("主持者", 2),
    PLANNER("策划者", 3),
    ;

    private final String role;

    private final int value;

    RecRoleEnum(String role, int value) {
        this.role = role;
        this.value = value;
    }

    // 通过role获取value
    public static Integer getRoleValue(String role) {
        for (RecRoleEnum recRoleEnum : RecRoleEnum.values()) {
            if (recRoleEnum.getRole().equals(role)) {
                return recRoleEnum.getValue();
            }
        }
        return null;
    }
}
