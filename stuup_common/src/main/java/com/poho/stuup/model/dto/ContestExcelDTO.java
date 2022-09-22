package com.poho.stuup.model.dto;


public class ContestExcelDTO {

    private int rowNum;

    private String stuNo;

    private String name;

    private String level;

    private String unitName;

    private String rank;

    private String obtainDate;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getObtainDate() {
        return obtainDate;
    }

    public void setObtainDate(String obtainDate) {
        this.obtainDate = obtainDate;
    }
}
