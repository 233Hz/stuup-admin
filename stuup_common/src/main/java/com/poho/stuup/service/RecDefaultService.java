package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.excel.RecDefaultExcel;

import java.util.List;

/**
 * <p>
 * 默认积分记录表（除综评表） 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
public interface RecDefaultService extends IService<RecDefault> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: batchCode
     * @param: excels
     * @return: void
     * @author BUNGA
     * @date: 2023/5/29 10:42
     */
    void saveRecDefaultExcel(List<RecDefaultExcel> excels, RecImportParams recImportParams);
}
