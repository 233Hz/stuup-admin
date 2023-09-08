package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ReviewOfEachClassVO {

    /**
     * 班级名称
     */
    private String className;

    /**
     * 申请数
     */
    private int applyCount;

    /**
     * 审核数
     */
    private int auditCount;

    /**
     * 班主任教师id
     */
    @JsonIgnore
    private Long classTeacherId;
}
