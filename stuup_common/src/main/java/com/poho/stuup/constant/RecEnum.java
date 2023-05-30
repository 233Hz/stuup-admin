package com.poho.stuup.constant;

import com.poho.stuup.handle.*;
import lombok.Getter;

@Getter
public enum RecEnum {

    REC_CAUCUS("rec_caucus", "参加党团学习项目记录填报", new RecCaucusHandle()),
    REC_HONOR("rec_honor", "人荣誉记录填报", new RecHonorHandle()),
    REC_LABOR_TIME("rec_labor_time", "生产劳动实践记录填报", new RecLaborTimeHandle()),
    REC_MILITARY("rec_military", "军事训练记录填报", new RecMilitaryHandle()),
    REC_NATION("rec_nation", "参加国防民防项目记录填报", new RecNationHandle()),
    REC_SOCIETY("rec_society", "参加社团记录填报", new RecSocietyHandle()),
    REC_VOLUNTEER("rec_volunteer", "志愿者活动记录填报", new RecVolunteerHandle());

    private final String code;

    private final String description;

    private final RecExcelHandle handle;


    RecEnum(String code, String description, RecExcelHandle handle) {
        this.code = code;
        this.description = description;
        this.handle = handle;
    }

    // 通过code获取枚举
    public static RecEnum getByCode(String code) {
        for (RecEnum recEnum : RecEnum.values()) {
            if (recEnum.getCode().equals(code)) {
                return recEnum;
            }
        }
        return null;
    }
}
