package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 全校个人排行榜搜索对象
 * @date 2023/6/2 13:21
 */
@Data
public class SchoolRankDTO {

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
    private Long gradeId;

    /**
     * 所属班级
     */
    private String className;

    /**
     * 所属系部
     */
    private Long facultyId;

    /**
     * 所属专业
     */
    private Long majorId;

}
