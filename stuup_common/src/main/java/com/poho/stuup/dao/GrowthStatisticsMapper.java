package com.poho.stuup.dao;

import com.poho.stuup.model.vo.GrowthStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author BUNGA
 * @description
 * @date 2023/9/12 15:59
 */
public interface GrowthStatisticsMapper {

    List<GrowthStatisticsVO> list(@Param("yearId") Long yearId, @Param("semesterId") Long semesterId, @Param("classIds") List<Integer> classIds);
}
