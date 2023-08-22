package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecSociety;
import com.poho.stuup.model.dto.RecSocietyDTO;
import com.poho.stuup.model.dto.SocietySaveDTO;
import com.poho.stuup.model.excel.RecSocietyExcel;
import com.poho.stuup.model.vo.RecSocietyVO;

import java.util.List;

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
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 13:35
     */
    void saveRecSocietyExcel(List<RecSocietyExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode);

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecSocietyVO>
     * @author BUNGA
     * @date: 2023/6/1 15:18
     */
    IPage<RecSocietyVO> getRecSocietyPage(Page<RecSocietyVO> page, RecSocietyDTO query);

    void saveSocietyFromSyncData(SocietySaveDTO societySaveDTO);
}
