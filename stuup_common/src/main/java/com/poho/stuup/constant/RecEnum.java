package com.poho.stuup.constant;

import lombok.Getter;

@Getter
public enum RecEnum {

    REC_CAUCUS("CZ_005", "参加党团学习项目记录填报", "参加党团学习导入模板"),
    REC_HONOR_NATIONAL("CZ_028", "个人荣誉记录填报国家级", "个人荣誉记导入模板"),
    REC_HONOR_MUNICIPAL_LEVEL("CZ_029", "个人荣誉记录填报市级", "个人荣誉记导入模板"),
    REC_HONOR_DISTRICT_LEVEL("CZ_030", "个人荣誉记录填报区级", "个人荣誉记导入模板"),
    REC_LABOR_TIME("CZ_065", "生产劳动实践记录填报", "生产劳动实践导入模板"),
    REC_MILITARY_EXCELLENT("CZ_009", "军事训练记录填报（优秀学员）", "军事训练导入模板"),
    REC_MILITARY_QUALIFIED("CZ_010", "军事训练记录填报（合格学员）", "军事训练导入模板"),
    REC_NATION("CZ_008", "参加国防民防项目记录填报", "参加国防民防导入模板"),
    REC_SOCIETY("CZ_011", "参加社团记录填报", "参加社团导入模板"),
    REC_VOLUNTEER("CZ_062", "志愿者活动记录填报", "志愿者活动导入模板");

    private final String code;

    private final String description;

    private final String tempName;


    RecEnum(String code, String description, String tempName) {
        this.code = code;
        this.description = description;
        this.tempName = tempName;
    }

    public static RecEnum getEnumByCode(String code) {
        for (RecEnum recEnum : RecEnum.values()) {
            if (recEnum.getCode().equals(code)) {
                return recEnum;
            }
        }
        return null;
    }
}
