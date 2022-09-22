package com.poho.stuup.model.dto;


import com.poho.stuup.model.Certificate;

import java.io.Serializable;

public class CertificateDTO extends Certificate implements Serializable {

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
