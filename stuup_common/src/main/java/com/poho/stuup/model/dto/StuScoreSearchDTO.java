package com.poho.stuup.model.dto;


import java.io.Serializable;

public class StuScoreSearchDTO implements Serializable {

    private Integer stuId;

    private String stuNo;

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }
}
