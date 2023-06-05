package com.poho.stuup.model.dto;

import lombok.Data;

@Data
public class RecCaucusDTO {

    /**
     * 学年
     */
    private Long yearId;

    /**
     * 年级
     */
    private Long gradeId;

    /**
     * 班级
     */
    private Long classId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;


}
