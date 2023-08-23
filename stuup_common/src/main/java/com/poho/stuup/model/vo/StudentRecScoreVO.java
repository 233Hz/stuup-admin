package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author BUNGA
 * @description: 学生积分获取记录对象
 * @date 2023/6/1 17:58
 */
@Data
public class StudentRecScoreVO {

    private Long id;

    private String name;

    /**
     * 获得分数
     */
    private BigDecimal score;

    /**
     * 获得时间
     */
    private Date createTime;

    /**
     * 说明
     */
    private String description;

    /**
     * 一级项目
     */
    @JsonIgnore
    private String firstName;

    /**
     * 二级项目
     */
    @JsonIgnore
    private String secondName;

    /**
     * 三级项目
     */
    @JsonIgnore
    private String thirdName;

    /**
     * 成长项名称
     */
    @JsonIgnore
    private String growName;


}
