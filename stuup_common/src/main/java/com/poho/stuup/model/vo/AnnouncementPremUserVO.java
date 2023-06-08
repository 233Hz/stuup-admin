package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 公告根据权限指定用户对象
 * @date 2023/6/8 18:36
 */
@Data
public class AnnouncementPremUserVO {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户类型
     */
    private String userType;

}
