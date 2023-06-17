package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 记录审核日志对象
 * @date 2023/6/15 18:26
 */
@Data
public class AuditLogVO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 状态
     */
    private int state;

    /**
     * 原因
     */
    private String reason;

    /**
     * 操作时间
     */
    private Date createTime;

}
