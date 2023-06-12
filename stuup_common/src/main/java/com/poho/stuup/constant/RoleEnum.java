package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("系统管理员", "ADMIN"),
    SCHOOL_LEADERS("校级领导", "SCHOOL_LEADERS"),
    DEPT_LEADERS("系部领导", "DEPT_LEADERS");

    private final String roleName;
    private final String roleCode;

    RoleEnum(String roleName, String roleCode) {
        this.roleName = roleName;
        this.roleCode = roleCode;
    }
}
