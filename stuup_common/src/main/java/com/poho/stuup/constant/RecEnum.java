package com.poho.stuup.constant;

import com.poho.stuup.handle.*;
import lombok.Getter;

@Getter
public enum RecEnum {

    REC_CAUCUS("CZ_005", "参加党团学习项目记录填报", new RecCaucusHandle()),
    REC_HONOR("CZ_031", "个人荣誉记录填报", new RecHonorHandle()),
    REC_LABOR_TIME("CZ_065", "生产劳动实践记录填报", new RecLaborTimeHandle()),
    REC_MILITARY_EXCELLENT("CZ_009", "军事训练记录填报（优秀学员）", new RecMilitaryHandle()),
    REC_MILITARY_QUALIFIED("CZ_010", "军事训练记录填报（合格学员）", new RecMilitaryHandle()),
    REC_NATION("CZ_008", "参加国防民防项目记录填报", new RecNationHandle()),
    REC_SOCIETY("CZ_011", "参加社团记录填报", new RecSocietyHandle()),
    REC_VOLUNTEER("CZ_060", "志愿者活动记录填报", new RecVolunteerHandle());

    private final String code;

    private final String description;

    private final RecExcelHandle handle;


    RecEnum(String code, String description, RecExcelHandle handle) {
        this.code = code;
        this.description = description;
        this.handle = handle;
    }

    public static RecEnum getEnumByCode(String code) {
        for (RecEnum recEnum : RecEnum.values()) {
            if (recEnum.getCode().equals(code)) {
                return recEnum;
            }
        }
        return null;
    }

    /**
     * @description: 通过code获取对应的处理器（没有则返回默认处理器）
     * @param: code
     * @return: com.poho.stuup.handle.RecExcelHandle
     * @author BUNGA
     * @date: 2023/6/14 10:14
     */
    public static RecExcelHandle getHandle(String code) {
        for (RecEnum recEnum : RecEnum.values()) {
            if (recEnum.getCode().equals(code)) {
                return recEnum.handle;
            }
        }
        return new RecDefaultHandle();
    }
}
