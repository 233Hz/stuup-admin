package com.poho.stuup.model.dto;

import lombok.Data;

@Data
public class GrowRecordDTO {

    /**
     * 年份id
     */
    private Long yearId;

    /**
     * 学期id
     */
    private Long semesterId;

    /**
     * 一级项目
     */
    private Integer l1Id;

    /**
     * 二级项目
     */
    private Integer l2Id;

    /**
     * 三级项目
     */
    private Integer l3Id;

    /**
     * 成长项
     */
    private String growthItemName;

    /**
     * 记录状态
     */
    private Integer state;

    /**
     * 当前用户id
     */
    private Long userId;

    /**
     * 审核类型
     */
    private Integer type;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 顺序/导入查询
     */
    private String sortOrder = "asc";

    /**
     * 申请人id
     */
    private Long applicantId;

    /**
     * 审核人id
     */
    private Long auditorId;

    /**
     * 提交人id
     */
    private Long submitterId;

}
