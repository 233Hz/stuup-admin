package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.model.vo.GrowthItemSelectVO;

import java.util.List;

/**
 * <p>
 * 成长项 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
public interface GrowthItemService extends IService<GrowthItem> {


    ResponseModel<List<GrowthItem>> getSelfApplyItem(String type, Long userId);

    /**
     * @description: 获取成长模型配置
     * @param:
     * @return: com.poho.stuup.model.vo.FlowerVO
     * @author BUNGA
     * @date: 2023/6/9 11:23
     */
    FlowerVO getFlowerConfig();

    /**
     * @description: 获取学生可申请的成长项
     * @param:
     * @return: java.util.List<com.poho.stuup.model.vo.GrowthItemSelectVO>
     * @author BUNGA
     * @date: 2023/6/15 15:09
     */
    List<GrowthItemSelectVO> getStudentGrowthItems();


    ResponseModel<Long> saveOrUpdateGrowthItem(GrowthItem data);

}
