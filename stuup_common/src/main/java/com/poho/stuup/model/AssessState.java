package com.poho.stuup.model;

import java.util.Date;

public class AssessState {
    private Long oid;

    private Long yearId;

    private Long assessUser;

    private Integer assessType;

    private Integer state;

    private Date createTime;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public Long getAssessUser() {
        return assessUser;
    }

    public void setAssessUser(Long assessUser) {
        this.assessUser = assessUser;
    }

    public Integer getAssessType() {
        return assessType;
    }

    public void setAssessType(Integer assessType) {
        this.assessType = assessType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}