package com.poho.stuup.model;


public class Military {

    private Long oid;

    private Integer stuId;

    private Integer level;

    private Integer goodFlag;

    private Integer status;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGoodFlag() {
        return goodFlag;
    }

    public void setGoodFlag(Integer goodFlag) {
        this.goodFlag = goodFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}