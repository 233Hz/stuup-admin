package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.GrowGathererEnum;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.GrowthRuleDescVO;
import com.poho.stuup.model.vo.UserApplyGrowthItemVO;

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

    /**
     * 获取用户的申请项目
     */
    List<UserApplyGrowthItemVO> getApplyGrowthItem(GrowGathererEnum type, Long userId);


    ResponseModel<Long> saveOrUpdateGrowthItem(GrowthItem data);

    List<GrowthRuleDescVO> getGrowthRuleDesc();
}
