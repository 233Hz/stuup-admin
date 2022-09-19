package com.poho.stuup.model;

public class Score {

    private Long oid;

   private Integer stuId;

   private Integer totalScore;

   private Integer currGradeScore;

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

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getCurrGradeScore() {
        return currGradeScore;
    }

    public void setCurrGradeScore(Integer currGradeScore) {
        this.currGradeScore = currGradeScore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}