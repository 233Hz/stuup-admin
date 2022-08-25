package com.poho.stuup.model;

import java.util.Date;

public class OperRecord {
    private Long oid;

    private Long yearId;

    private Long operUser;

    private Date operTime;

    private Integer normCode;

    private Integer operType;

    private String operUserName;

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

    public Long getOperUser() {
        return operUser;
    }

    public void setOperUser(Long operUser) {
        this.operUser = operUser;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public Integer getNormCode() {
        return normCode;
    }

    public void setNormCode(Integer normCode) {
        this.normCode = normCode;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName;
    }
}