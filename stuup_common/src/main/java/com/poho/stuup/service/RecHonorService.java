package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecHonor;
import com.poho.stuup.model.dto.RecHonorDTO;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.vo.RecHonorVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 人荣誉记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecHonorService extends IService<RecHonor> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 13:49
     */
    void saveRecHonorExcel(long batchCode, GrowthItem growthItem, List<RecHonorExcel> excels, Map<String, Object> params);

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecHonorVO>
     * @author BUNGA
     * @date: 2023/6/1 15:22
     */
    IPage<RecHonorVO> getRecHonorPage(Page<RecHonorVO> page, RecHonorDTO query);
}
