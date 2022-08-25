package com.poho.stuup.model;

import java.util.Date;

public class Register {
    private Long oid;

    private Long userId;

    private Long yearId;

    private String position;

    private String job;

    private String summary;

    private String leaderOpinion;

    private String groupOpinion;

    private String orgOpinion;

    private String note;

    private Integer state;

    private Date createTime;

    private String userName;
    private String yearName;
    private String deptName;
    private String degree;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public String getLeaderOpinion() {
        return leaderOpinion;
    }

    public void setLeaderOpinion(String leaderOpinion) {
        this.leaderOpinion = leaderOpinion == null ? null : leaderOpinion.trim();
    }

    public String getGroupOpinion() {
        return groupOpinion;
    }

    public void setGroupOpinion(String groupOpinion) {
        this.groupOpinion = groupOpinion == null ? null : groupOpinion.trim();
    }

    public String getOrgOpinion() {
        return orgOpinion;
    }

    public void setOrgOpinion(String orgOpinion) {
        this.orgOpinion = orgOpinion == null ? null : orgOpinion.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}