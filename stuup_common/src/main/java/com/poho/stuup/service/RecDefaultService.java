package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.excel.RecDefaultExcel;

import java.util.Date;
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
     * @param: batchCode
     * @param: excels
     * @return: void
     * @author BUNGA
     * @date: 2023/5/29 10:42
     */
    void saveRecDefaultExcel(long batchCode, GrowthItem growthItem, List<RecDefaultExcel> excels, Map<String, Object> params);

    /**
     * @description: 查找该成长项目该时间段内的导入记录
     * @param: growId           成长项目id
     * @param: startTime    开始时间
     * @param: endTime      结束时间
     * @return: java.util.List<java.lang.Long>  返回记录的学生id集合
     * @author BUNGA
     * @date: 2023/6/6 15:14
     */
    List<Long> findGrowStudentRecForTimePeriod(Long growId, Date startTime, Date endTime);
}
