package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecNation;
import com.poho.stuup.model.excel.RecNationExcel;

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
     * 保存导入数据
     *
     * @param data
     * @return
     */
    boolean saveData(RecNationExcel data);
}
