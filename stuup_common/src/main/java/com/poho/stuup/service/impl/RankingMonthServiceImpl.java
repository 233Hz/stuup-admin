package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RankingMonthMapper;
import com.poho.stuup.model.RankingMonth;
import com.poho.stuup.model.dto.ProgressRankDTO;
import com.poho.stuup.model.vo.ProgressRankVO;
import com.poho.stuup.service.RankingMonthService;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public IPage<ProgressRankVO> getProgressRanking(Page<ProgressRankVO> page, ProgressRankDTO query) {
        // 默认查询上月
        DateTime lastMonth = DateUtil.offset(new Date(), DateField.MONTH, 1);
        if (query.getYear() == null) {
            int year = DateUtil.year(lastMonth);
            query.setYear(year);
        }
        if (query.getMonth() == null) {
            int month = DateUtil.month(lastMonth);
            query.setMonth(month);
        }

        IPage<ProgressRankVO> iPage = baseMapper.getProgressRanking(page, query);
        return null;
    }
}
