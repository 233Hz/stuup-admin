package com.poho.stuup.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author BUNGA
 * @description: 全校个人排行榜搜索对象
 * @date 2023/6/2 13:21
 */
@Data
public class SchoolStuRankDTO {

    /**
     * 学年
     */
    @NotNull(message = "学年不能为空")
    private Long yearId;

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
    private String facultyId;

}
