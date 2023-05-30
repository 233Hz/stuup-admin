package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecVolunteer;
import com.poho.stuup.model.excel.RecVolunteerExcel;

/**
 * <p>
 * 志愿者活动记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecVolunteerService extends IService<RecVolunteer> {

    /**
     * @description: 保存导入数据
     * @param: data
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/26 9:26
     */
    boolean saveData(RecVolunteerExcel data);
}
