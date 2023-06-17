package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 审核日志对象
 * @date 2023/6/15 18:09
 */

@Data
public class AudLogDTO {

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
    private Long thirdLevelId;

    /**
     * 成长项
     */
    private String growName;

    /**
     * 状态（2.提交 3.通过 4.拒绝 5.退回）
     */
    private Integer state;

    /**
     * 当前用户id
     */
    private Long userId;

}
