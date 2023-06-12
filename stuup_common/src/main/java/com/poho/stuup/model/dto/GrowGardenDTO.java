package com.poho.stuup.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GrowGardenDTO {

    /**
     * 花园类型
     */
    @NotNull(message = "花园类型不能为空")
    private int gardenType;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 所属年级
     */
    private Long gradeId;

    /**
     * 所属班级
     */
    private String className;


}
