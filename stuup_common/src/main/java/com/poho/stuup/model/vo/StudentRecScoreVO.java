package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 学生积分获取记录对象
 * @date 2023/6/1 17:58
 */
@Data
public class StudentRecScoreVO {

    private Long id;

    /**
     * 获取学年
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
     * 获得分数
     */
    private Integer score;

    /**
     * 获得时间
     */
    private Date createTime;

}
