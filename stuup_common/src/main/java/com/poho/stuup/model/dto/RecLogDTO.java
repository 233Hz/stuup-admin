package com.poho.stuup.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author BUNGA
 * @description: 导入日志搜索对象
 * @date 2023/5/31 14:18
 */
@Data
public class RecLogDTO {

    /**
     * 学年
     */
    private Long yearId;

    /**
     * 一级项目
     */
    private Long firstLevelId;

    /**
     * 二级项目
     */
    private Long secondLevelId;

    /**
     * 三级项目
     */
    private Long threeLevelId;

    /**
     * 成长项目
     */
    private String growName;

    @JsonIgnore
    private Long userId;
}
