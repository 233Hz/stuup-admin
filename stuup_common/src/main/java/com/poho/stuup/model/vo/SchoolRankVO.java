package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 全校学生排名对象
 * @date 2023/6/2 13:20
 */
@Data
public class SchoolRankVO {

    /**
     * 学生id
     */
    private Long id;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 年级
     */
    private String gradeName;

    /**
     * 所属班级
     */
    private String className;

    /**
     * 班主任
     */
    private String classTeacher;

    /**
     * 所属系部
     */
    private String facultyName;

    /**
     * 所属专业
     */
    private String majorName;

    /**
     * 成长值
     */
    private int score;


}
