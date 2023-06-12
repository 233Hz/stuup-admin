package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RankingMonth;
import com.poho.stuup.model.dto.ProgressRankDTO;
import com.poho.stuup.model.vo.ProgressRankVO;

/**
 * <p>
 * 学期排行榜 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
public interface RankingMonthService extends IService<RankingMonth> {

    /**
     * @description: 分页查询进步榜
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.ProgressRankVO>
     * @author BUNGA
     * @date: 2023/6/12 19:22
     */
    IPage<ProgressRankVO> getProgressRanking(Page<ProgressRankVO> page, ProgressRankDTO query);
}
