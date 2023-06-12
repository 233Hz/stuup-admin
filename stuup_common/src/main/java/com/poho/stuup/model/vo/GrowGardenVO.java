package com.poho.stuup.model.vo;

import lombok.Data;

@Data
public class GrowGardenVO {

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 所属年级
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
     * 成长值
     */
    private Integer score;

}
