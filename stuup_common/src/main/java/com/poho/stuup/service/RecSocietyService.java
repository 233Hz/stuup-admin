package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecSociety;
import com.poho.stuup.model.excel.RecSocietyExcel;

/**
 * <p>
 * 参加社团记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecSocietyService extends IService<RecSociety> {

    /**
     * 保存导入数据
     *
     * @param data
     * @return
     */
    boolean saveData(RecSocietyExcel data);
}
