package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 审核记录对象
 * @date 2023/6/15 16:46
 */
@Data
public class GrowAuditRecordVO {

    /**
     * id
     */
    private Long id;

    /**
     * 成长项id
     */
    private Long growId;

    /**
     * 一级项目
     */
    private String firstLevelName;

    /**
     * 二级项目
     */
    private String secondLevelName;

    /**
     * 三级项目
     */
    private String thirdLevelName;

    /**
     * 成长项
     */
    private String growName;

    /**
     * 申请人
     */
    private String applicant;

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

}
