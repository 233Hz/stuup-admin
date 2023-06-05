package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RecNationVO {

    private Long id;

    /**
     * 学年
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
     * 项目名称
     */
    private String name;

    /**
     * 获奖级别
     */
    private Integer level;

    /**
     * 组织机构（主办方）
     */
    private String org;

    /**
     * 累计时间（课时）
     */
    private Integer hour;

    /**
     * 创建时间
     */
    private Date createTime;

}
