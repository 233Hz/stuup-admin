package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.model.vo.GrowthItemSelectVO;

import java.util.List;
import java.util.Map;

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
     * @description: 获取成长项目Map
     * @param:
     * @return: java.util.Map<java.lang.String, java.lang.Long>
     * @author BUNGA
     * @date: 2023/5/26 10:34
     */
    Map<String, Long> getGrowthItemMap();

    /**
     * @description: 判断名称和编号是否重复
     * @param: title
     * @param: code
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/29 16:27
     */
    boolean isExist(String code);

    /**
     * @description: 判断名称和编号是否重复
     * @param: id
     * @param: title
     * @param: code
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/29 17:48
     */
    boolean isExist(Long id, String code);

    /**
     * @description: 通过code查询成长项
     * @param: recCode
     * @return: com.poho.stuup.model.GrowthItem
     * @author BUNGA
     * @date: 2023/5/30 14:19
     */
    GrowthItem getGrowthItemByCode(String recCode);

    /**
     * @description: 查询用户可导入对象
     * @param: userId
     * @return: java.util.List<com.poho.stuup.model.vo.SelectorVO>
     * @author BUNGA
     * @date: 2023/5/30 18:44
     */
    ResponseModel<List<GrowthItem>> getUserGrowthItems(Long userId);

    /**
     * @description: 校验该用户可导入次数
     * @param: userId
     * @param: growCode
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/31 8:48
     */
    boolean verifyRemainingFillNum(Long userId, String growCode);

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
}
