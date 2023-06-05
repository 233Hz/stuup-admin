package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 军事训练记录填报
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Data
public class RecMilitaryVO {

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
     * 等级
     */
    private Integer level;

    /**
     * 是否优秀
     */
    private Integer excellent;

    /**
     * 创建时间
     */
    private Date createTime;

}
