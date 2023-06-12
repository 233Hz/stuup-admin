package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RankingYear;
import com.poho.stuup.model.dto.YearRankDTO;
import com.poho.stuup.model.vo.YearRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 年度排行榜 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Mapper
public interface RankingYearMapper extends BaseMapper<RankingYear> {

    IPage<YearRankVO> getYearRanking(Page<YearRankVO> page, @Param("yearId") Long yearId, @Param("query") YearRankDTO query);
}
