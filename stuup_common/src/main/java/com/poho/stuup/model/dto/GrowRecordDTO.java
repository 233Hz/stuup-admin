package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 申请记录查询对象
 * @date 2023/6/15 16:46
 */
@Data
public class GrowRecordDTO {

    /**
     * 一级项目
     */
    private Integer firstLevelId;

    /**
     * 二级项目
     */
    private Integer secondLevelId;

    /**
     * 三级项目
     */
    private Integer thirdLevelId;

    /**
     * 成长项
     */
    private String growName;

    /**
     * 记录状态
     */
    private Integer state;

    /**
     * 当前用户id
     */
    private Long userId;

}
