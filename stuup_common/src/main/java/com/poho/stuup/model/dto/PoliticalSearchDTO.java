package com.poho.stuup.model.dto;


import java.io.Serializable;

public class PoliticalSearchDTO extends PageDTO implements Serializable {

    private String stuNo;

    private String stuName;

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
}
