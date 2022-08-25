package com.poho.stuup.model;

import java.util.Date;

public class StandardCategory {
    private Long oid;

    private String categoryName;

    private Integer categoryScore;

    private Long createUser;

    private Date createTime;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Integer getCategoryScore() {
        return categoryScore;
    }

    public void setCategoryScore(Integer categoryScore) {
        this.categoryScore = categoryScore;
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
}