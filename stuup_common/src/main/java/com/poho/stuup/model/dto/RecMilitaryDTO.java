package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * <p>
 * 军事训练记录填报
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Data
public class RecMilitaryDTO {

    /**
     * 学年
     */
    private Long yearId;

    /**
     * 年级
     */
    private Long gradeId;

    /**
     * 班级
     */
    private Long classId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 是否优秀
     */
    private Integer excellent;

}
