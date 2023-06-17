package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AudGrowVO {

    /**
     * id
     */
    private Long id;

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
