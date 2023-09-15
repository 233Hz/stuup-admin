package com.poho.stuup.model.dto;


import lombok.Data;

/**
 * @author BUNGA
 * @date 2023/6/1 17:59
 */
@Data
public class RecScoreDTO {

    /**
     * 获取学年
     */
    private Long yearId;

    /**
     * 学期id
     */
    private Long semesterId;

    /**
     * 一级名称
     */
    private Long l1Id;

    /**
     * 二级名称
     */
    private Long l2Id;

    /**
     * 三级名称
     */
    private Long l3Id;

    /**
     * 成长项目
     */
    private String growthItemName;

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
