package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 审核日志对象
 * @date 2023/6/15 18:09
 */

@Data
public class AudLogVO {

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
     * 状态（1.提交 2.通过 3.拒绝 4.退回）
     */
    private Integer state;

    /**
     * 操作时间
     */
    private Date createTime;

}
