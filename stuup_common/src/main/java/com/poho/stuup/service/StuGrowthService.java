package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.StuGrowth;

/**
 * <p>
 * 学生成长项目相关信息 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-04
 */
public interface StuGrowthService extends IService<StuGrowth> {

    /**
     * 采集次数
     *
     * @param studentId
     */
    void addCollectCount(Long studentId, Long growthItemId);
}
