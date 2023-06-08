package com.poho.stuup.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author BUNGA
 * @description: 公告根据权限指定用户查询对象
 * @date 2023/6/8 18:36
 */
@Data
public class AnnouncementPremUserDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 当前用户
     */
    @JsonIgnore
    private Long currUser;

}
