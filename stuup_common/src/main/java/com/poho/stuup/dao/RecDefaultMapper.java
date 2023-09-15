package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.vo.RecLogDetailsVO;
import com.poho.stuup.model.vo.StudentGrowthMonitorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

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

    List<RecLogDetailsVO> getRecLogDetails(@Param("batchCode") Long batchCode);

    List<StudentGrowthMonitorVO> countViolationsTop3(@Param("growthItemIds") Set<Long> growthItemIds, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int countImportGrowth(@Param("growthItemIds") List<Long> growthItemIds, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
