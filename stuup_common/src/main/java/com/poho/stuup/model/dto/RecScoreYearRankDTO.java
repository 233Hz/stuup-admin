package com.poho.stuup.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecScoreYearRankDTO {

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 获得的积分
     */
    private BigDecimal score;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 年级id
     */
    private Integer gradeId;

    /**
     * 专业id
     */
    private Integer majorId;

}
