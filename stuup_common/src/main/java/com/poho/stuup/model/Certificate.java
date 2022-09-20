package com.poho.stuup.model;

import java.util.Date;

public class Certificate {

    private Long oid;

    private Integer sutId;

    private String name;

    private Integer level;

    private String unitName;

    private String major;

    private String certNo;

    private Date obtainDate;

    private Integer status;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Integer getSutId() {
        return sutId;
    }

    public void setSutId(Integer sutId) {
        this.sutId = sutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public Date getObtainDate() {
        return obtainDate;
    }

    public void setObtainDate(Date obtainDate) {
        this.obtainDate = obtainDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}