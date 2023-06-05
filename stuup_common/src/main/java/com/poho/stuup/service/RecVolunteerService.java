package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecVolunteer;
import com.poho.stuup.model.dto.RecVolunteerDTO;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.model.vo.RecVolunteerVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 志愿者活动记录填报 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
public interface RecVolunteerService extends IService<RecVolunteer> {

    /**
     * @description: 保存导入数据
     * @param: batchCode
     * @param: growthItem
     * @param: excels
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/1 14:37
     */
    void saveRecVolunteerExcel(long batchCode, GrowthItem growthItem, List<RecVolunteerExcel> excels, Map<String, Object> params);

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecVolunteerVO>
     * @author BUNGA
     * @date: 2023/6/2 13:54
     */
    IPage<RecVolunteerVO> getVolunteerPage(Page<RecVolunteerVO> page, RecVolunteerDTO query);
}
