package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 全校top10排行
 * @date 2023/8/9 9:28
 */

@Data
public class WholeSchoolTop10VO {

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 班级姓名
     */
    private String className;

    /**
     * 班主任
     */
    private String classTeacher;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 积分
     */
    private BigDecimal score;
}
