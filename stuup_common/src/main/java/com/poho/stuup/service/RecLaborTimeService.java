package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecLaborTime;
import com.poho.stuup.model.excel.RecLaborTimeExcel;

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
     * @param: data
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/25 15:49
     */
    boolean saveData(RecLaborTimeExcel data);
}
