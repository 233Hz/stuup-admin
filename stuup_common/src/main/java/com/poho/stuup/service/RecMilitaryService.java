package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.model.excel.RecMilitaryExcel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 军事训练记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecMilitaryService extends IService<RecMilitary> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:28
     */
    void saveRecMilitaryExcel(long batchCode, GrowthItem growthItem, List<RecMilitaryExcel> excels, Map<String, Object> params);
}
