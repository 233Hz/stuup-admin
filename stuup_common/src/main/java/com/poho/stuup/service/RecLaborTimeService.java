package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecLaborTime;
import com.poho.stuup.model.excel.RecLaborTimeExcel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生产劳动实践记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecLaborTimeService extends IService<RecLaborTime> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:15
     */
    void saveRecLaborTimeExcel(long batchCode, GrowthItem growthItem, List<RecLaborTimeExcel> excels, Map<String, Object> params);
}
