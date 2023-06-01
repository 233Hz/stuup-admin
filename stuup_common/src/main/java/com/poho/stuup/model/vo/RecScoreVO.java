package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/1 17:58
 */
@Data
public class RecScoreVO {
    
    private Long id;

    /**
     * 获取学年
     */
    private String yearName;

    /**
     * 一级名称
     */
    private String firstLevelName;

    /**
     * 二级名称
     */
    private String secondLevelName;

    /**
     * 三级名称
     */
    private String threeLevelName;

    /**
     * 成长项目
     */
    private String growName;

    /**
     * 年级
     */
    private String gradeName;

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
     * 证件号
     */
    private String idCard;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 获得分数
     */
    private Integer score;

    /**
     * 获得时间
     */
    private Date createTime;

}
