package com.poho.stuup.model.dto;


import lombok.Data;

/**
 * @author BUNGA
 * @description: 学生积分获取搜索对象
 * @date 2023/6/1 17:59
 */
@Data
public class StudentRecScoreDTO {

    /**
     * 学年id
     */
    private Long yearId;

    /**
     * 一级名称
     */
    private Long firstLevelId;

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

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

}
