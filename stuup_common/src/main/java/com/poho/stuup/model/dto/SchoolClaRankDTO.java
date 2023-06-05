package com.poho.stuup.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/4 13:54
 */
@Data
public class SchoolClaRankDTO {

    @NotNull(message = "寻不能为空")
    private Long yearId;

    /**
     * 年级
     */
    private String gradeId;

    /**
     * 所属班级
     */
    private String className;

    /**
     * 所属系部
     */
    private String facultyId;

}
