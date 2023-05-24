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


    /**
     * 上学期结束时间
     */
    private Date lastSemester;
    /**
     * 下学期开始时间
     */
    private Date nextSemester;

    public Date getLastSemester() {
        return lastSemester;
    }

    public void setLastSemester(Date lastSemester) {
        this.lastSemester = lastSemester;
    }

    public Date getNextSemester() {
        return nextSemester;
    }

    public void setNextSemester(Date nextSemester) {
        this.nextSemester = nextSemester;
    }
}
