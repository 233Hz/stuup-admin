package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.model.dto.RecCaucusDTO;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.model.vo.RecCaucusVO;

import java.util.List;

/**
 * <p>
 * 参加党团学习项目记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecCaucusService extends IService<RecCaucus> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:08
     */
    void saveRecCaucusExcel(List<RecCaucusExcel> excels, RecImportParams params);

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecCaucusVO>
     * @author BUNGA
     * @date: 2023/6/2 13:33
     */
    IPage<RecCaucusVO> getRecCaucusPage(Page<RecCaucusVO> page, RecCaucusDTO query);
}
