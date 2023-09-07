package com.poho.stuup.model.dto;

import lombok.Data;

@Data
public class GrowthItemUserDTO {


    /**
     * 用户名
     */
    private String username;

    /**
     * 教师工号
     */
    private String teacherNo;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 班级id
     */
    private Integer classId;

}
