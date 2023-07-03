package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RankMonth;
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
public interface RankMonthService extends IService<RankMonth> {

    List<ProgressRankVO> getProgressRank();
}
