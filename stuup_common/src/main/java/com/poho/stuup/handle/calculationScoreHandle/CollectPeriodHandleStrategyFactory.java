package com.poho.stuup.handle.calculationScoreHandle;

import com.poho.stuup.constant.PeriodEnum;

/**
 * @author BUNGA
 * @description: 采集周期策略工厂
 * @date 2023/8/3 17:12
 */
public class CollectPeriodHandleStrategyFactory {

    public static CollectPeriodHandle CollectPeriodHandle(PeriodEnum periodEnum) {
        if (periodEnum == PeriodEnum.UNLIMITED) {
            return new UnlimitedPeriodStrategy();
        } else {
            return new LimitPeriodStrategy();
        }
    }
}
