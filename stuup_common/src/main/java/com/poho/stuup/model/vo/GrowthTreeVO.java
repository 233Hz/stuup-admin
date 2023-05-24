package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/5/24 14:20
 */
@Data
public class GrowthTreeVO {

    /**
     * 项目id
     */
    private Long id;

    /**
     * 父节点id
     */
    private Long pid;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 顺序
     */
    private Integer sort;

    private List<GrowthTreeVO> children;
}
