package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.*;

import java.util.List;

/**
 * @author BUNGA
 * @description: 学生画像 接口
 * @date 2023/8/7 9:15
 */
public interface PortraitService {

    /**
     * 基础信息
     *
     * @param userId
     * @return
     */
    ResponseModel<PortraitBasicInfoVO> getBasicInfo(Long userId);

    /**
     * 成长能力
     *
     * @param userId
     * @return
     */
    ResponseModel<List<PortraitCapacityEvaluatorVO>> getCapacityEvaluator(Long userId);

    /**
     * 获奖记录
     *
     * @param userId
     * @return
     */
    ResponseModel<List<PortraitAwardRecordVO>> getAwardRecord(Long userId);

    /**
     * 参加活动记录
     *
     * @param userId
     * @return
     */
    ResponseModel<List<PortraitActivityRecordVO>> getActivityRecord(Long userId);

    /**
     * 学期成长排名曲线图
     *
     * @param userId
     * @return
     */
    ResponseModel<List<PortraitRankingCurveVO>> getRankingCurve(Long userId);

    /**
     * 成长分析
     *
     * @param userId
     * @return
     */
    ResponseModel<List<PortraitGrowthAnalysisVO>> getGrowthAnalysis(Long userId);

    /**
     * 成长数据
     *
     * @param userId
     * @param semesterId
     * @return
     */
    ResponseModel<PortraitGrowthDataVO> getGrowthData(Long userId, Long semesterId);

    /**
     * 成长对比
     *
     * @param userId
     * @param semesterId
     * @return
     */
    ResponseModel<List<PortraitGrowthComparisonVO>> getGrowthComparison(Long userId, Long semesterId);

    /**
     * 学习成绩
     *
     * @param userId
     * @return
     */
    ResponseModel<List<PortraitStudyGradeVO>> getStudyGrade(Long userId);

    /**
     * 学习课程
     *
     * @param userId
     * @param semesterId
     * @return
     */
    ResponseModel<List<PortraitStudyCourseVO>> getStudyCourse(Long userId, Long semesterId);
}
