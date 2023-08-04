package com.poho.stuup.handle.calculationScoreHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 积分计算策略工厂
 * @date 2023/8/3 16:58
 */
public class CalculateScoreHandleStrategyFactory {
    private static final Map<CalculateRuleEnum, CalculateScoreHandle> map = new HashMap<>();

    static {
        map.put(CalculateRuleEnum.PLUS_ANYTIME, new CalculatePlusAnytimeStrategy());
        map.put(CalculateRuleEnum.PLUS_FIXED_NUM, new CalculatePlusFixedNumStrategy());
        map.put(CalculateRuleEnum.PLUS_PERIOD_LIMIT, new CalculatePlusPeriodLimitStrategy());
        map.put(CalculateRuleEnum.MINUS_ANYTIME, new CalculateMinusAnytimeStrategy());
        map.put(CalculateRuleEnum.MINUS_FIXED_NUM, new CalculateMinusFixedNumStrategy());
        map.put(CalculateRuleEnum.MINUS_PERIOD_LIMIT, new CalculateMinusPeriodLimitStrategy());
    }

    public static CalculateScoreHandle getCalculateScoreRuleExtension(CalculateRuleEnum calculateRuleEnum) {
        return map.get(calculateRuleEnum);
    }
}
