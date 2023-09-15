package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @date 2023/6/1 15:08
 */
@Data
public class RecSocietyDTO {

    /**
     * 年份
     */
    private Long yearId;

    /**
     * 年级
     */
    private Long gradeId;

    /**
     * 班级
     */
    private String className;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 社团名称
     */
    private String name;

    /**
     * 获奖级别
     */
    private Integer level;

    /**
     * 角色
     */
    private Integer role;

}
