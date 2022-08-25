package com.poho.stuup.model;

import java.util.Date;

public class AssessRecord {
    private Long oid;

    private Long yearId;

    private Long assessUser;

    private Long userId;

    private Integer assessType;

    private Integer score;

    private Integer adjustScore;

    private Long normId;

    private Integer state;

    private Date createTime;

    private String userName;
    private Integer userType;
    private String yearName;
    private String deptName;
    private Integer retire;
    private String note;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAssessType() {
        return assessType;
    }

    public void setAssessType(Integer assessType) {
        this.assessType = assessType;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getAdjustScore() {
        return adjustScore;
    }

    public void setAdjustScore(Integer adjustScore) {
        this.adjustScore = adjustScore;
    }

    public Long getNormId() {
        return normId;
    }

    public void setNormId(Long normId) {
        this.normId = normId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getRetire() {
        return retire;
    }

    public void setRetire(Integer retire) {
        this.retire = retire;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}