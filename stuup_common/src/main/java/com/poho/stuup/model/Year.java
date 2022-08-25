package com.poho.stuup.model;

import java.util.Date;

public class Year {
    private Long oid;

    private String yearName;

    private Date yearStart;

    private Date yearEnd;

    private Integer curr;

    private Date createTime;

    private Long createUser;

    private String dateRange;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName == null ? null : yearName.trim();
    }

    public Date getYearStart() {
        return yearStart;
    }

    public void setYearStart(Date yearStart) {
        this.yearStart = yearStart;
    }

    public Date getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Date yearEnd) {
        this.yearEnd = yearEnd;
    }

    public Integer getCurr() {
        return curr;
    }

    public void setCurr(Integer curr) {
        this.curr = curr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}