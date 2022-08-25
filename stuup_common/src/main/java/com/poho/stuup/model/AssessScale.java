package com.poho.stuup.model;

import java.util.Date;

public class AssessScale {
    private Long oid;

    private Long yearId;

    private Long deptId;

    private Integer aMin;

    private Integer aMax;

    private Integer bMin;

    private Integer bMax;

    private Integer cMin;

    private Integer cMax;

    private Integer dMin;

    private Integer dMax;

    private Long createUser;

    private Date createTime;

    private String deptName;
    private String yearName;
    private int total;

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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getaMin() {
        return aMin;
    }

    public void setaMin(Integer aMin) {
        this.aMin = aMin;
    }

    public Integer getaMax() {
        return aMax;
    }

    public void setaMax(Integer aMax) {
        this.aMax = aMax;
    }

    public Integer getbMin() {
        return bMin;
    }

    public void setbMin(Integer bMin) {
        this.bMin = bMin;
    }

    public Integer getbMax() {
        return bMax;
    }

    public void setbMax(Integer bMax) {
        this.bMax = bMax;
    }

    public Integer getcMin() {
        return cMin;
    }

    public void setcMin(Integer cMin) {
        this.cMin = cMin;
    }

    public Integer getcMax() {
        return cMax;
    }

    public void setcMax(Integer cMax) {
        this.cMax = cMax;
    }

    public Integer getdMin() {
        return dMin;
    }

    public void setdMin(Integer dMin) {
        this.dMin = dMin;
    }

    public Integer getdMax() {
        return dMax;
    }

    public void setdMax(Integer dMax) {
        this.dMax = dMax;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}