package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AnnouncementVO {

    /**
     * id
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型
     */
    private Integer type;

    /**
     * 文本类容
     */
    private String content;

    /**
     * 发布用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;


}
