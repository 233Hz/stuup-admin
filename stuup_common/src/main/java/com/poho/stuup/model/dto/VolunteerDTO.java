package com.poho.stuup.model.dto;


import com.poho.stuup.model.Volunteer;

import java.io.Serializable;

public class VolunteerDTO extends Volunteer implements Serializable {

    private String stuNo;

    private String stuName;

    private String durationName;

    private String operDateStr;

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

    public String getDurationName() {
        return durationName;
    }

    public void setDurationName(String durationName) {
        this.durationName = durationName;
    }

    public String getOperDateStr() {
        return operDateStr;
    }

    public void setOperDateStr(String operDateStr) {
        this.operDateStr = operDateStr;
    }
}
