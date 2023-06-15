package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 简化用户页面对象
 * @date 2023/6/14 19:11
 */
@Data
public class SimpleUserDTO {


    /**
     * 用户名
     */
    private String username;

    /**
     * 教师工号
     */
    private String teacherNo;

    /**
     * 所属部门
     */
    private Long deptId;
}
