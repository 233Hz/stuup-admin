package com.poho.stuup.service;

import cn.hutool.core.date.StopWatch;
import com.poho.stuup.model.vo.*;

import java.util.List;

public interface ScreenService {

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

    /**
     * 统计上月成长总分和上上月的对比
     *
     * @return
     */
    List<GrowthScoreCountVO> countGrowthScoreCompare();

    /**
     * 统计各专业人数
     *
     * @return
     */
    List<MajorPopulationsVO> countMajorPopulations();

    /**
     * 查询可视化重要突出的数据
     *
     * @return
     */
    ScreenImportantDataVO getImportantData(StopWatch stopWatch);

    /**
     * 统计近30天每日访问量
     *
     * @return
     */
    List<DailyVisitsVO> countDailyVisits();

    /**
     * 统计各班级的申请数和审核数
     *
     * @return
     */
    List<ReviewOfEachClassVO> countReviewOfEachClass();

    /**
     * 统计本月访问量
     *
     * @return
     */
    Integer countVisitsThisMonth();
}
