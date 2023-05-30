package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.RecScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 积分记录表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Mapper
public interface RecScoreMapper extends BaseMapper<RecScore> {

    /**
     * @description: 查找时间段内的记录
     * @param: timePeriod
     * @return: java.util.List<com.poho.stuup.model.RecScore>
     * @author BUNGA
     * @date: 2023/5/30 14:31
     */
    List<RecScore> findTimePeriodRecord(@Param("growId") Long growId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
