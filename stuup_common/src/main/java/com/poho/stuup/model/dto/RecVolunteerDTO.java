package com.poho.stuup.model.dto;

import lombok.Data;

@Data
public class RecVolunteerDTO {

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
     * 基地/项目名称
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;
}
