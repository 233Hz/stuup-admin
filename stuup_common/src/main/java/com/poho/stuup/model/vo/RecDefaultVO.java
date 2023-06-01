package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 页面默认记录对象
 * @date 2023/5/31 23:37
 */
@Data
public class RecDefaultVO {

    private Long id;

    /**
     * 年级
     */
    private String gradeName;

    /**
     * 班级
     */
    private String className;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 项目名称
     */
    private String growName;
    

    /**
     * 获得时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
