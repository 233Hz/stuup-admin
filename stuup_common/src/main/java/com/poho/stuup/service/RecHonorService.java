package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecHonor;
import com.poho.stuup.model.excel.RecHonorExcel;

/**
 * <p>
 * 人荣誉记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecHonorService extends IService<RecHonor> {

    /**
     * @description: 保存导入数据
     * @param: data
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/25 15:48
     */
    boolean saveData(RecHonorExcel data);
}
