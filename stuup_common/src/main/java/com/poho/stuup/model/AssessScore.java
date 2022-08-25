package com.poho.stuup.model;

import java.util.Date;

public class AssessScore {
    private Long oid;

    private Long yearId;

    private Long userId;

    private Double qzcp;

    private Double adjustQzcp;

    private Integer qzcprs;

    private Double zphp;

    private Integer zphprs;

    private Double fgld;

    private Integer fgldrs;

    private Double dzld;

    private Integer dzldrs;

    private Double score;

    private Double adjustScore;

    private Integer ranking;

    private Date createTime;

    private String userName;
    private Integer userType;
    private String yearName;
    private String deptName;
    private String rank;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getQzcp() {
        return qzcp;
    }

    public void setQzcp(Double qzcp) {
        this.qzcp = qzcp;
    }

    public Double getAdjustQzcp() {
        return adjustQzcp;
    }

    public void setAdjustQzcp(Double adjustQzcp) {
        this.adjustQzcp = adjustQzcp;
    }

    public Integer getQzcprs() {
        return qzcprs;
    }

    public void setQzcprs(Integer qzcprs) {
        this.qzcprs = qzcprs;
    }

    public Double getZphp() {
        return zphp;
    }

    public void setZphp(Double zphp) {
        this.zphp = zphp;
    }

    public Integer getZphprs() {
        return zphprs;
    }

    public void setZphprs(Integer zphprs) {
        this.zphprs = zphprs;
    }

    public Double getFgld() {
        return fgld;
    }

    public void setFgld(Double fgld) {
        this.fgld = fgld;
    }

    public Integer getFgldrs() {
        return fgldrs;
    }

    public void setFgldrs(Integer fgldrs) {
        this.fgldrs = fgldrs;
    }

    public Double getDzld() {
        return dzld;
    }

    public void setDzld(Double dzld) {
        this.dzld = dzld;
    }

    public Integer getDzldrs() {
        return dzldrs;
    }

    public void setDzldrs(Integer dzldrs) {
        this.dzldrs = dzldrs;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getAdjustScore() {
        return adjustScore;
    }

    public void setAdjustScore(Double adjustScore) {
        this.adjustScore = adjustScore;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}