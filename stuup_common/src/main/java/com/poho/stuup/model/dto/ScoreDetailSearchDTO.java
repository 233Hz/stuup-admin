package com.poho.stuup.model.dto;


import java.io.Serializable;

public class ScoreDetailSearchDTO extends PageDTO implements Serializable {

    private String name;

    private String stuName;

    private Integer level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
