package com.poho.stuup.constant;

import lombok.Getter;

/**
 * 花朵成长阶段枚举类
 */
@Getter
public enum FloweringStageEnum {

    BMH_SEED("bmh_seed_required_points", "白梅花种子兑换所需积分", "bmhSeed"),
    BMH_SPROUT("bmh_sprout_required_points", "白梅花发芽兑换所需积分", "bmhSprout"),
    BMH_BLOOM("bmh_bloom_required_points", "白梅花开花兑换所需积分", "bmhBloom"),
    BMH_FRUIT("bmh_fruit_required_points", "白梅花结果兑换所需积分", "bmhFruit"),
    XCJ_SEED("xcj_seed_required_points", "小雏菊种子兑换所需积分", "xcjSeed"),
    XCJ_SPROUT("xcj_sprout_required_points", "小雏菊发芽兑换所需积分", "xcjSprout"),
    XCJ_BLOOM("xcj_bloom_required_points", "小雏菊开花兑换所需积分", "xcjBloom"),
    XCJ_FRUIT("xcj_fruit_required_points", "小雏菊结果兑换所需积分", "xcjFruit"),
    XHH_SEED("xhh_seed_required_points", "西红花种子兑换所需积分", "xhhSeed"),
    XHH_SPROUT("xhh_sprout_required_points", "西红花发芽兑换所需积分", "xhhSprout"),
    XHH_BLOOM("xhh_bloom_required_points", "西红花开花兑换所需积分", "xhhBloom"),
    XHH_FRUIT("xhh_fruit_required_points", "西红花结果兑换所需积分", "xhhFruit");

    private final String configKey;
    private final String description;
    private final String fieldName;

    FloweringStageEnum(String configKey, String description, String fieldName) {
        this.configKey = configKey;
        this.description = description;
        this.fieldName = fieldName;
    }

    // 通过 fieldName 获取枚举
    public static FloweringStageEnum getByFieldName(String fieldName) {
        for (FloweringStageEnum recLevelEnum : FloweringStageEnum.values()) {
            if (recLevelEnum.getFieldName().equals(fieldName)) {
                return recLevelEnum;
            }
        }
        return null;
    }
}
