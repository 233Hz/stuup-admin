package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RankingMonth;
import com.poho.stuup.model.dto.ProgressRankDTO;
import com.poho.stuup.model.vo.ProgressRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 学期排行榜 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Mapper
public interface RankingMonthMapper extends BaseMapper<RankingMonth> {

    IPage<ProgressRankVO> getProgressRanking(Page<ProgressRankVO> page, @Param("query") ProgressRankDTO query);
}
