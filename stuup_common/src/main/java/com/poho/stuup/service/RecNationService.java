package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecNation;
import com.poho.stuup.model.excel.RecNationExcel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参加国防民防项目记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecNationService extends IService<RecNation> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: recNationExcels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:32
     */
    void saveRecNationExcel(long batchCode, GrowthItem growthItem, List<RecNationExcel> excels, Map<String, Object> params);
}
