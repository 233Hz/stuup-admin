package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RankingYear;
import com.poho.stuup.model.dto.YearRankDTO;
import com.poho.stuup.model.vo.YearRankVO;

/**
 * <p>
 * 年度排行榜 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
public interface RankingYearService extends IService<RankingYear> {

    IPage<YearRankVO> getYearRanking(Page<YearRankVO> page, Long yearId, YearRankDTO query);
}
