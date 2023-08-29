package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 每月进步榜对象
 * @date 2023/6/2 13:20
 */
@Data
public class ProgressRankVO {

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 所属班级
     */
    private String className;

    /**
     * 班主任
     */
    private String classTeacher;

    /**
     * 成长值
     */
    private BigDecimal score;

    /**
     * 排名变化名次
     */
    private Integer rankChange;

    /**
     * 本月获取分数
     */
    private BigDecimal scoreChange;

    @JsonIgnore
    private Integer classId;
    @JsonIgnore
    private Integer gradeId;
    @JsonIgnore
    private Integer majorId;
    @JsonIgnore
    private Long avatarId;

}
