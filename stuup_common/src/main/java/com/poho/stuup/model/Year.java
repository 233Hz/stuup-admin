package com.poho.stuup.model;

import lombok.Data;

import java.util.Date;

@Data
public class Year {
    private Long oid;

    private Integer year;

    private String yearName;

    private Date yearStart;

    private Date yearEnd;

    private Integer curr;

    private Date createTime;

    private Long createUser;

    /**
     * 上学期结束时间
     */
    private Date lastSemester;
    /**
     * 下学期开始时间
     */
    private Date nextSemester;

}
