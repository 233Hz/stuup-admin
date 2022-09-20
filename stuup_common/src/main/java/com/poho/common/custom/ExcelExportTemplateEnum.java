package com.poho.common.custom;

public enum ExcelExportTemplateEnum {

    REWARD("template/reward.xlsx","奖励信息")
    ,CONTEST("template/contest.xlsx","技能大赛信息")
    ,CERTIFICATE("template/certificate.xlsx","考证信息")
    ,MILITARY("template/military.xlsx","军训信息")
    ,POLITICAL("template/political.xlsx","党团活动信息")
    ,VOLUNTEER("template/volunteer.xlsx","志愿者服务信息");

    ExcelExportTemplateEnum(String path, String name) {
        this.path = path;
        this.name = name;
    };
    private String path;

    private String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
