package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.vo.RecLogDetailsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 默认积分记录表（除综评表） Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
@Mapper
public interface RecDefaultMapper extends BaseMapper<RecDefault> {

    List<Long> findGrowStudentRecForTimePeriod(@Param("growId") Long growId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<RecLogDetailsVO> getRecLogDetails(@Param("batchCode") Long batchCode);
}
