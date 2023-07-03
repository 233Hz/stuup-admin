package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 发布系统消息对象
 * @date 2023/6/26 14:41
 */
@Data
public class SystemMagVO {

    /**
     * 消息标题
     */
    private String title;

    /**
     * 发布用户
     */
    private Long userId;
}
