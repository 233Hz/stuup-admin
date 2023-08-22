package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.poho.stuup.dao.RecAddScoreMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.vo.WholeClassTop10VO;
import com.poho.stuup.model.vo.WholeSchoolTop10VO;
import com.poho.stuup.service.RankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RankServiceImpl implements RankService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private RecAddScoreMapper recAddScoreMapper;

    @Override
    public List<WholeSchoolTop10VO> getWholeSchoolTop10Ranking() {
        Long yearId = yearMapper.findCurrYearId();
        if (yearId == null) return new ArrayList<>();
        List<WholeSchoolTop10VO> list = recAddScoreMapper.findWholeSchoolTop10Ranking(yearId);
        if (CollUtil.isNotEmpty(list)) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                WholeSchoolTop10VO wholeSchoolTop10 = list.get(i);
                wholeSchoolTop10.setRanking(i + 1);
            }
        }
        return list;
    }

    @Override
    public List<WholeClassTop10VO> getWholeClassTop10Ranking() {
        Long yearId = yearMapper.findCurrYearId();
        if (yearId == null) return new ArrayList<>();
        List<WholeClassTop10VO> list = recAddScoreMapper.findWholeClassTop10Ranking(yearId);
        if (CollUtil.isNotEmpty(list)) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                WholeClassTop10VO wholeClassTop10VO = list.get(i);
                wholeClassTop10VO.setRanking(i + 1);
            }
        }
        return list;
    }
}
