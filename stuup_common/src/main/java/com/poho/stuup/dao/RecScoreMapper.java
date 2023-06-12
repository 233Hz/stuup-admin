package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;
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

    IPage<RecScoreVO> getRecScorePage(Page<RecScoreVO> page, @Param("query") RecScoreDTO query);

    /**
     * @description: 查找时间段内的记录
     * @param: startTime
     * @param: endTime
     * @return: java.util.List<com.poho.stuup.model.RecScore>
     * @author BUNGA
     * @date: 2023/6/12 18:51
     */
    List<RecScore> findTimePeriodRecord(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * @description: 查找时间段内的记录
     * @param: growId
     * @param: startTime
     * @param: endTime
     * @return: java.util.List<com.poho.stuup.model.RecScore>
     * @author BUNGA
     * @date: 2023/6/12 19:08
     */
    List<RecScore> findTimePeriodRecordForGrow(@Param("growId") Long growId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
