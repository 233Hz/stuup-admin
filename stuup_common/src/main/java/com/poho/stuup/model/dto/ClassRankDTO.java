package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/4 13:54
 */
@Data
public class ClassRankDTO {

    /**
     * 班级名称
     */
    private String className;

    /**
     * 所属年级
     */
    private Long gradeId;

    /**
     * 所属系部
     */
    private Long facultyId;

    /**
     * 所属专业
     */
    private Long majorId;

}
