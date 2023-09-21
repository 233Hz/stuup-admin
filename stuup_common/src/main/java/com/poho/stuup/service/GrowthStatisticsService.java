package com.poho.stuup.service;

import com.poho.stuup.model.vo.GrowthStatisticsVO;

import java.util.List;

/**
 * @author BUNGA
 * @date 2023/9/12 15:52
 */
public interface GrowthStatisticsService {

    List<GrowthStatisticsVO> list(Long userId, Long yearId, Long semesterId);
}
