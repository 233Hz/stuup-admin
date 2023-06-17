package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 成长项选择对象
 * @date 2023/6/15 15:06
 */
@Data
public class GrowthItemSelectVO {

    /**
     * id
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

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
}
