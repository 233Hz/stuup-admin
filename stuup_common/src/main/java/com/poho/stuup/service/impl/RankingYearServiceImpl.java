package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RankingYearMapper;
import com.poho.stuup.model.RankingYear;
import com.poho.stuup.model.dto.YearRankDTO;
import com.poho.stuup.model.vo.YearRankVO;
import com.poho.stuup.service.RankingYearService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 年度排行榜 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Service
public class RankingYearServiceImpl extends ServiceImpl<RankingYearMapper, RankingYear> implements RankingYearService {


    @Override
    public IPage<YearRankVO> getYearRanking(Page<YearRankVO> page, Long yearId, YearRankDTO query) {
        return baseMapper.getYearRanking(page, yearId, query);
    }
}
