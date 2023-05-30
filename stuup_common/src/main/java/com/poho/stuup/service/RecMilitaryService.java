package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.model.excel.RecMilitaryExcel;

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
     * @param: data
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/26 8:35
     */
    boolean saveData(RecMilitaryExcel data);
}
