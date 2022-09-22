package com.poho.stuup.model.dto;

import com.poho.stuup.model.Political;

import java.io.Serializable;

public class PoliticalDTO extends Political implements Serializable {

    private Integer gradeId;

    private String gradeName;

    private Integer classId;

    private String className;

    private String stuNo;

    private String stuName;

    private String levelName;

    private String durationDateStr;

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getDurationDateStr() {
        return durationDateStr;
    }

    public void setDurationDateStr(String durationDateStr) {
        this.durationDateStr = durationDateStr;
    }
}
