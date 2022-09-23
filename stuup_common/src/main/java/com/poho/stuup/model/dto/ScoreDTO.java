package com.poho.stuup.model.dto;


import com.poho.stuup.model.Score;

import java.io.Serializable;

public class ScoreDTO extends Score implements Serializable {

    private String stuName;

    private String levelName;

    private String obtainDateStr;

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

    public String getObtainDateStr() {
        return obtainDateStr;
    }

    public void setObtainDateStr(String obtainDateStr) {
        this.obtainDateStr = obtainDateStr;
    }
}
