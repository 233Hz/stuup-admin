package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.Growth;
import com.poho.stuup.model.vo.GrowthTreeVO;

import java.util.List;

/**
 * <p>
 * 成长项目表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
public interface GrowthService extends IService<Growth> {

    /**
     * @description: 获取成长项目树
     * @param:
     * @return: java.util.List<com.poho.stuup.model.vo.GrowthTreeVO>
     * @author BUNGA
     * @date: 2023/5/24 14:24
     */
    List<GrowthTreeVO> getGrowthTree();

}
