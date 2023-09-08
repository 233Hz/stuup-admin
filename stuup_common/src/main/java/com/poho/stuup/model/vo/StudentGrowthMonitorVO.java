package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author BUNGA
 * @description: 学生成长监控可视化对象
 * @date 2023/7/19 18:05
 */
@Data
public class StudentGrowthMonitorVO {

    /**
     * 成长项名称
     */
    private String growItemName;

    /**
     * 违规人次
     */
    private Long personNum;

    /**
     * 项目id
     */
    @JsonIgnore
    private Long growthItemId;

}
