package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * 统计学生申请项目数
 */
@Data
public class CountStudentApplyDTO {

    private Long studentId;

    private Long count;

}
