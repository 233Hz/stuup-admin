package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author BUNGA
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
    private String l1;

    /**
     * 二级名称
     */
    private String l2;

    /**
     * 三级名称
     */
    private String l3;

    /**
     * 成长项目
     */
    private String growthItemName;

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
    private BigDecimal score;

    /**
     * 获得时间
     */
    private Date createTime;

    @JsonIgnore
    private Long yearId;

    @JsonIgnore
    private Long semesterId;

    @JsonIgnore
    private Long l1Id;

    @JsonIgnore
    private Long l2Id;

    @JsonIgnore
    private Long l3Id;

    @JsonIgnore
    private Integer gradeId;

}
