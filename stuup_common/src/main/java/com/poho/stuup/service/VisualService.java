package com.poho.stuup.service;

import com.poho.stuup.model.vo.AllKindsOfCompetitionAwardNumVO;
import com.poho.stuup.model.vo.GrowthScoreCountVO;
import com.poho.stuup.model.vo.StudentGrowthMonitorVO;
import com.poho.stuup.model.vo.YearAtSchoolNumVO;

import java.util.List;

public interface VisualService {

    /**
     * 学生成长监控
     *
     * @return
     */
    List<StudentGrowthMonitorVO> studentGrowthMonitor();

    /**
     * 近三年在校生人数
     *
     * @return
     */
    List<YearAtSchoolNumVO> countNear3YearsAtSchoolNum();

    /**
     * 各类竞赛获奖次数
     *
     * @return
     */
    List<AllKindsOfCompetitionAwardNumVO> countAllKindsOfCompetitionAwardNum();

    /**
     * 获得奖学金人数
     *
     * @return
     */
    Long countScholarshipNum();

    /**
     * 举办活动次数
     *
     * @return
     */
    Long countHoldAnActivityNum();

    List<GrowthScoreCountVO> countGrowthScoreForLastMonth();
}
