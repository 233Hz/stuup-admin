package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/1 15:19
 */
@Data
public class RecHonorDTO {

    private Long id;

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
     * 荣誉称号
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

}
