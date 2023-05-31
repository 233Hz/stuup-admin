package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.dto.GrowSearchDTO;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.model.vo.GrowRecordVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 默认积分记录表（除综评表） 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
public interface RecDefaultService extends IService<RecDefault> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growId
     * @param: excels
     * @return: void
     * @author BUNGA
     * @date: 2023/5/29 10:42
     */
    void saveRecDefault(long batchCode, GrowthItem growthItem, List<RecDefaultExcel> excels, Map<String, Object> params);

    /**
     * @description: 分页查询导入的项目
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.dto.GrowSearchDTO>
     * @author BUNGA
     * @date: 2023/5/31 13:26
     */
    IPage<GrowRecordVO> growthRecordPage(Page page, GrowSearchDTO query);
}
