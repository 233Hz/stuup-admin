package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RecCaucusVO {

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
     * 活动名称
     */
    private String name;

    /**
     * 级别
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
