package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.ProgressStateEnum;
import com.poho.stuup.dao.RankingMonthMapper;
import com.poho.stuup.model.RankingMonth;
import com.poho.stuup.model.dto.ProgressRankDTO;
import com.poho.stuup.model.vo.ProgressRankVO;
import com.poho.stuup.service.RankingMonthService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学期排行榜 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Service
public class RankingMonthServiceImpl extends ServiceImpl<RankingMonthMapper, RankingMonth> implements RankingMonthService {

    @Override
    public List<ProgressRankVO> getProgressRanking(ProgressRankDTO query) {
        // 默认查询上月
        DateTime lastMonth = DateUtil.offset(new Date(), DateField.MONTH, -1);
        Integer year = null, month = null;
        if (query.getYear() == null) {
            year = DateUtil.year(lastMonth);
            query.setYear(year);
        }
        if (query.getMonth() == null) {
            month = DateUtil.month(lastMonth);
            query.setMonth(month);
        }
        List<ProgressRankVO> currentMonthRankings = baseMapper.getProgressRanking(query);

        if (CollUtil.isNotEmpty(currentMonthRankings)) {
            if (query.getYear() == null) {
                year = DateUtil.year(lastMonth);
                query.setYear(year - 1);
            }
            if (query.getMonth() == null) {
                month = DateUtil.month(lastMonth);
                query.setMonth(month - 1 == 0 ? 12 : month - 1);
            }

            List<ProgressRankVO> previousMonthRankings = baseMapper.getProgressRanking(query);
            if (CollUtil.isNotEmpty(previousMonthRankings)) {
                Map<Long, ProgressRankVO> previousMonthRankingMap = previousMonthRankings.stream()
                        .collect(Collectors.toMap(ProgressRankVO::getId, progressRankVO -> progressRankVO));


                currentMonthRankings.forEach(currentMonthRanking -> {
                    long studentId = currentMonthRanking.getId();
                    int currentRanking = currentMonthRanking.getRanking();
                    ProgressRankVO previousMonthRanking = previousMonthRankingMap.get(studentId);

                    if (previousMonthRanking != null) {
                        int previousRanking = previousMonthRanking.getRanking();

                        // 积分为0 不比较
                        if (currentMonthRanking.getScore().compareTo(BigDecimal.ZERO) == 0 || previousMonthRanking.getScore().compareTo(BigDecimal.ZERO) == 0)
                            return;


                        if (currentRanking < previousRanking) {
                            currentMonthRanking.setProgressState(ProgressStateEnum.UP.getValue());
                            currentMonthRanking.setProgressRanking(previousRanking - currentRanking);
                        } else if (currentRanking > previousRanking) {
                            currentMonthRanking.setProgressState(ProgressStateEnum.DOWN.getValue());
                            currentMonthRanking.setProgressRanking(currentRanking - previousRanking);
                        } else {
                            currentMonthRanking.setProgressState(ProgressStateEnum.SAME.getValue());
                            currentMonthRanking.setProgressRanking(0);
                        }
                    }
                });

                return currentMonthRankings.stream()
                        .sorted(Comparator.comparing(ProgressRankVO::getProgressState)
                                .thenComparing(ProgressRankVO::getProgressRanking))
                        .collect(Collectors.toList());
            }

            return currentMonthRankings;
        }

        return null;
    }
}
