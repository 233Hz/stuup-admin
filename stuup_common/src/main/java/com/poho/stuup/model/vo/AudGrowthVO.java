package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AudGrowthVO {

    /**
     * id
     */
    private Long id;

    /**
     * 学年
     */
    private String yearName;

    /**
     * 学期
     */
    private String semesterName;

    /**
     * 一级项目
     */
    private String l1Name;

    /**
     * 二级项目
     */
    private String l2Name;

    /**
     * 三级项目
     */
    private String l3Name;

    /**
     * 成长项id
     */
    private Long growthItemId;

    /**
     * 成长项
     */
    private String growthItemName;

    /**
     * 项目积分
     */
    private BigDecimal growthItemScore;

    /**
     * 申请人
     */
    private String applicantName;

    /**
     * 审核人
     */
    private String auditorName;

    /**
     * 提交人
     */
    private String submitterName;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 文件ids
     */
    private String fileIds;

    /**
     * 记录状态
     */
    private Integer state;

    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 一级id
     */
    @JsonIgnore
    private Long l1Id;

    /**
     * 二级id
     */
    @JsonIgnore
    private Long l2Id;

    /**
     * 三级id
     */
    @JsonIgnore
    private Long l3Id;

    /**
     * 申请人id
     */
    @JsonIgnore
    private Long applicantId;

    /**
     * 审核人id
     */
    @JsonIgnore
    private Long auditorId;

    /**
     * 提交人id
     */
    @JsonIgnore
    private Long submitterId;

    /**
     * 班级id
     */
    @JsonIgnore
    private Integer classId;

    /**
     * 学年id
     */
    @JsonIgnore
    private Long yearId;

    /**
     * 学期id
     */
    @JsonIgnore
    private Long semesterId;

}
