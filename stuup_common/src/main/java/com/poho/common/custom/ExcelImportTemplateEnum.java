package com.poho.common.custom;

public enum ExcelImportTemplateEnum {

    REWARD("template/reward.xlsx","奖励信息导入模板.xlsx")
    ,CONTEST("template/contest.xlsx","技能大赛信息导入模板.xlsx")
    ,CERTIFICATE("template/certificate.xlsx","考证信息导入模板.xlsx")
    ,MILITARY("template/military.xlsx","军训信息导入模板.xlsx")
    ,POLITICAL("template/political.xlsx","党团活动信息导入模板.xlsx")
    ,VOLUNTEER("template/volunteer.xlsx","志愿者服务信息导入模板.xlsx");

    ExcelImportTemplateEnum(String path, String name) {
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
