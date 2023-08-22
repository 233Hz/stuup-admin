package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.model.dto.RecMilitaryDTO;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.model.vo.RecMilitaryVO;

import java.util.List;

/**
 * <p>
 * 军事训练记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecMilitaryService extends IService<RecMilitary> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:28
     */
    void saveRecMilitaryExcel(List<RecMilitaryExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode);

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecMilitaryVO>
     * @author BUNGA
     * @date: 2023/6/2 13:47
     */
    IPage<RecMilitaryVO> getRecMilitaryPage(Page<RecMilitaryVO> page, RecMilitaryDTO query);
}
