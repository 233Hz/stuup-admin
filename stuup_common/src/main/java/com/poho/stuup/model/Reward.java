package com.poho.stuup.model;

import java.util.Date;

public class Reward {

    private Long oid;

    private Integer stuId;

    private String name;

    private Integer level;

    private String unitName;

    private String rank;

    private Integer status;

    private Date obtainDate;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getObtainDate() {
        return obtainDate;
    }

    public void setObtainDate(Date obtainDate) {
        this.obtainDate = obtainDate;
    }
}