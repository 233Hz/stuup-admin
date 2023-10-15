package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.RecLaborTime;
import com.poho.stuup.model.dto.RecLaborTimeDTO;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.model.vo.RecLaborTimeVO;

import java.util.List;

/**
 * <p>
 * 生产劳动实践记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecLaborTimeService extends IService<RecLaborTime> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:15
     */
    void saveRecLaborTimeExcel(List<RecLaborTimeExcel> excels, RecImportParams params);

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecLaborTimeVO>
     * @author BUNGA
     * @date: 2023/6/2 13:59
     */
    IPage<RecLaborTimeVO> getRecLaborTimePage(Page<RecLaborTimeVO> page, RecLaborTimeDTO query);
}
