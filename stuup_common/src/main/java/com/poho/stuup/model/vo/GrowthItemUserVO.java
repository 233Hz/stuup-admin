package com.poho.stuup.model.vo;

import lombok.Data;

@Data
public class GrowthItemUserVO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 教师工号
     */
    private String loginName;

    /**
     * 所属部门
     */
    private String deptName;

    /**
     * 班级名称
     */
    private String className;
}
