package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.model.excel.RecCaucusExcel;

/**
 * <p>
 * 参加党团学习项目记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecCaucusService extends IService<RecCaucus> {

    /**
     * @description: 保存导入数据
     * @param: data
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/25 15:48
     */
    boolean saveData(RecCaucusExcel data);
}
