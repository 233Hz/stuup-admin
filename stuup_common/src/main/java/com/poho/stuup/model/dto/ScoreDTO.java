package com.poho.stuup.model.dto;


import com.poho.stuup.model.Score;

import java.io.Serializable;

public class ScoreDTO extends Score implements Serializable {

    private Integer gradeId;

    private String gradeName;

    private Integer classId;

    private String className;

    private String stuNo;

    private String stuName;

    private Integer tomatoNum = 0; //西红花数量

    private Integer wintersweetNum = 0; //梅花数量

    private Integer daisyNum = 0; //小雏菊数量


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

    public Integer getTomatoNum() {
        return tomatoNum;
    }

    public void setTomatoNum(Integer tomatoNum) {
        this.tomatoNum = tomatoNum;
    }

    public Integer getWintersweetNum() {
        return wintersweetNum;
    }

    public void setWintersweetNum(Integer wintersweetNum) {
        this.wintersweetNum = wintersweetNum;
    }

    public Integer getDaisyNum() {
        return daisyNum;
    }

    public void setDaisyNum(Integer daisyNum) {
        this.daisyNum = daisyNum;
    }
}
