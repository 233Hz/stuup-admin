package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecNation;
import com.poho.stuup.model.dto.RecNationDTO;
import com.poho.stuup.model.excel.RecNationExcel;
import com.poho.stuup.model.vo.RecNationVO;

import java.util.List;

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
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: recNationExcels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:32
     */
    void saveRecNationExcel(List<RecNationExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode);

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecNationVO>
     * @author BUNGA
     * @date: 2023/6/2 13:41
     */
    IPage<RecNationVO> getRecNationPage(Page<RecNationVO> page, RecNationDTO query);
}
