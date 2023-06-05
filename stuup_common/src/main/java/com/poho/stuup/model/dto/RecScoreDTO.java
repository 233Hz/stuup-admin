package com.poho.stuup.model.dto;


import lombok.Data;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/1 17:59
 */
@Data
public class RecScoreDTO {

    /**
     * 获取学年
     */
    private Long yearId;

    /**
     * 一级名称
     */
    private Long firstLevelId;

    /**
     * 二级名称
     */
    private Long secondLevelId;

    /**
     * 三级名称
     */
    private Long threeLevelId;

    /**
     * 成长项目
     */
    private String growName;

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
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
