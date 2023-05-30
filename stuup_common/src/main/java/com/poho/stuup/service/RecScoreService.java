package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecScore;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分记录表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface RecScoreService extends IService<RecScore> {

    /**
     * @description: 计算成长积分
     * @param: studentIds
     * @param: growId
     * @return: void
     * @author BUNGA
     * @date: 2023/5/29 13:08
     */
    void calculateScore(List<Long> studentIds, GrowthItem growthItem, Map<String, Object> params);

    /**
     * 查找该时间段内学生获取的分数
     *
     * @param growthId  成长项id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return Map<学生id, 学生获得的分数>
     */
    Map<Long, Integer> findTimePeriodScoreMap(Long growthId, Date startTime, Date endTime);
}
