package com.poho.stuup.model;

import java.util.Date;

public class StandardNorm {
    private Long oid;

    private Long categoryId;

    private String normName;

    private Integer normScore;

    private String evaluation;

    private Integer normSort;

    private Long createUser;

    private Date createTime;

    private String categoryName;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getNormName() {
        return normName;
    }

    public void setNormName(String normName) {
        this.normName = normName == null ? null : normName.trim();
    }

    public Integer getNormScore() {
        return normScore;
    }

    public void setNormScore(Integer normScore) {
        this.normScore = normScore;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation == null ? null : evaluation.trim();
    }

    public Integer getNormSort() {
        return normSort;
    }

    public void setNormSort(Integer normSort) {
        this.normSort = normSort;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}