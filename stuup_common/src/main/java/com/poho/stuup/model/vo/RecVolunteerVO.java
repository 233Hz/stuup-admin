package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RecVolunteerVO {

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
     * 基地/项目名称
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 子项目
     */
    private String child;

    /**
     * 岗位
     */
    private String post;

    /**
     * 学时
     */
    private Integer studyTime;

    /**
     * 服务时间
     */
    private Date serviceTime;

    /**
     * 晚填理由
     */
    private String reason;

    /**
     * 创建时间
     */
    private Date createTime;

}
