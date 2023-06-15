package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RankingMonth;
import com.poho.stuup.model.dto.ProgressRankDTO;
import com.poho.stuup.model.vo.ProgressRankVO;

import java.util.List;

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
     * @description: 查询进步榜
     * @param: query
     * @return: java.util.List<com.poho.stuup.model.vo.ProgressRankVO>
     * @author BUNGA
     * @date: 2023/6/12 22:59
     */
    List<ProgressRankVO> getProgressRanking(ProgressRankDTO query);
}
