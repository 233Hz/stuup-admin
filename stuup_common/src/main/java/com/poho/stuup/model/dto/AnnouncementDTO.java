package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/6/8 17:08
 */
@Data
public class AnnouncementDTO {

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型
     */
    private Integer type;
}