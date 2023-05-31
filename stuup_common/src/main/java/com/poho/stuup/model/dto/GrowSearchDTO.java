package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 成长项目记录搜索对象
 * @date 2023/5/31 14:18
 */
@Data
public class GrowSearchDTO {

    /**
     * 一级名称
     */
    private Long firstLeveId;

    /**
     * 二级名称
     */
    private Long secondLevelId;

    /**
     * 三级名称
     */
    private Long threeLevelId;

    /**
     * 成长项目
     */
    private String growName;
}
