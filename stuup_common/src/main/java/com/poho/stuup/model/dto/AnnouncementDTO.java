package com.poho.stuup.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author BUNGA
 * @description: 通知传输对象
 * @date 2023/6/8 17:08
 */
@Data
public class AnnouncementDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    private String title;

    /**
     * 公告类型
     */
    private Integer type;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告范围
     */
    @NotNull(message = "公告范围不能为空")
    private Integer scope;

    /**
     * 是否发布
     */
    private boolean publish;

    /**
     * 消息发布状态
     */
    @JsonIgnore
    private Integer state;

    @JsonIgnore
    private Long userId;

}
