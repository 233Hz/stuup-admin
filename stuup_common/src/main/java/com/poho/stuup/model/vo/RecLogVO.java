package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 页面导入成长记录对象
 * @date 2023/5/31 13:28
 */

@Data
public class RecLogVO {

    /**
     * 标记
     */
    private Long id;

    /**
     * 学年
     */
    private String yearName;

    /**
     * 一级名称
     */
    private String firstLevelName;

    /**
     * 二级名称
     */
    private String secondLevelName;

    /**
     * 三级名称
     */
    private String threeLevelName;

    /**
     * 成长项目
     */
    private String growName;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 导入时间
     */
    private Date createTime;

    private Long batchCode;

}
