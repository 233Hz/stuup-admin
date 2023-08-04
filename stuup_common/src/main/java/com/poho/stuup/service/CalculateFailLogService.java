package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.CalculateFailLog;
import com.poho.stuup.model.GrowthItem;

/**
 * <p>
 * 积分计算失败记录 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-03
 */
public interface CalculateFailLogService extends IService<CalculateFailLog> {

    void saveCalculateResult(CalculateFailLog calculateFailLog, GrowthItem growthItem);
}
