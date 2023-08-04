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
     * 获得分数
     */
    private Integer score;

    /**
     * 获得时间
     */
    private Date createTime;

    /**
     * 说明
     */
    private String description;

}
