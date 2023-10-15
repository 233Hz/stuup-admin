package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 成长项选择对象
 * @date 2023/6/15 15:06
 */
@Data
public class UserApplyGrowthItemVO {

    /**
     * id
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 一级项目id
     */
    private Long l1Id;

    /**
     * 二级项目id
     */
    private Long l2Id;

    /**
     * 三级项目id
     */
    private Long l3Id;

    /**
     * 一级项目
     */
    private String l1Name;

    /**
     * 二级项目
     */
    private String l2Name;

    /**
     * 三级项目
     */
    private String l3Name;
}
