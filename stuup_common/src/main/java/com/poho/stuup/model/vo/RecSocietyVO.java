package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/1 15:08
 */
@Data
public class RecSocietyVO {

    private Long id;

    /**
     * 年份
     */
    private String yearName;

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
     * 社团名称
     */
    private String name;

    /**
     * 获奖级别
     */
    private Integer level;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;
}
