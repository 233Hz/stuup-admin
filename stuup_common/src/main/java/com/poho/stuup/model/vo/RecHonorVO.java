package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/1 15:19
 */
@Data
public class RecHonorVO {

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
     * 荣誉称号
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 评选单位
     */
    private String unit;

    /**
     * 评选时间
     */
    private Date time;

    /**
     * 创建时间
     */
    private Date createTime;

}
