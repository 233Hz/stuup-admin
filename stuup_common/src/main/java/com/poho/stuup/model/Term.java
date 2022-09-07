package com.poho.stuup.model;

import java.util.Date;

public class Term {
    /**
    * 唯一标识
    */
    private Integer id;

    /**
    * 学期名称
    */
    private String name;

    /**
    * 学期年度
    */
    private String year;

    /**
     * 学期序号
     */
    private Integer termNo;

    /**
    * 开始时间
    */
    private Date beginTime;

    /**
    * 结束时间
    */
    private Date endTime;

    /**
    * 是否有效:1、有效；2、无效
    */
    private Integer isValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getTermNo() {
        return termNo;
    }

    public void setTermNo(Integer termNo) {
        this.termNo = termNo;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

}