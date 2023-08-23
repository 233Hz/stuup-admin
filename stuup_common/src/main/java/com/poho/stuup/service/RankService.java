package com.poho.stuup.service;

import com.poho.stuup.model.vo.ProgressTop10VO;
import com.poho.stuup.model.vo.WholeClassTop10VO;
import com.poho.stuup.model.vo.WholeSchoolTop10VO;

import java.util.List;

public interface RankService {

    /**
     * 获取当前学年全校top10排名
     *
     * @return
     */
    List<WholeSchoolTop10VO> getWholeSchoolTop10Ranking();

    List<WholeClassTop10VO> getWholeClassTop10Ranking();

    List<ProgressTop10VO> getProgressTop10Ranking();
}
