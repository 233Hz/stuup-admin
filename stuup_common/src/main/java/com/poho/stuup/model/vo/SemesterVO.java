package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author BUNGA
 * @description: 学期页面对象
 * @date 2023/6/27 10:08
 */
@Data
public class SemesterVO {

    /**
     * id
     */
    private Long id;

    /**
     * 所属学年id
     */
    private Long yearId;

    /**
     * 所属学年
     */
    private String yearName;

    /**
     * 学期名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 是否为当前学期
     */
    private Integer isCurrent;

    /**
     * 创建时间
     */
    private Date createTime;

}
