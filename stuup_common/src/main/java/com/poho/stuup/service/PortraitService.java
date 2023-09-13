package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.User;
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
     * @param student
     * @return
     */
    PortraitBasicInfoVO getBasicInfo(Student student, User user);

    /**
     * 成长能力
     *
     * @param studentId
     * @return
     */
    List<PortraitCapacityEvaluatorVO> getCapacityEvaluator(Long studentId);

    /**
     * 获奖记录
     *
     * @param studentId
     * @return
     */
    List<PortraitAwardRecordVO> getAwardRecord(Long studentId);

    /**
     * 参加活动记录
     *
     * @param studentId
     * @return
     */
    List<PortraitActivityRecordVO> getActivityRecord(Long studentId);

    /**
     * 学期成长排名曲线图
     *
     * @param student
     * @return
     */
    ResponseModel<List<PortraitRankingCurveVO>> getRankingCurve(Student student);

    /**
     * 成长分析
     *
     * @param student
     * @return
     */
    ResponseModel<List<PortraitGrowthAnalysisVO>> getGrowthAnalysis(Student student);

    /**
     * 成长数据
     *
     * @param studentId
     * @param semesterId
     * @return
     */
    PortraitGrowthDataVO getGrowthData(Integer studentId, Long semesterId);

    /**
     * 成长对比
     *
     * @param semesterId
     * @param studentId
     * @return
     */
    List<PortraitGrowthComparisonVO> getGrowthComparison(Long studentId, Long semesterId);

    /**
     * 学习成绩
     *
     * @param student
     * @return
     */
    ResponseModel<List<PortraitStudyGradeVO>> getStudyGrade(Student student);

    /**
     * 学习课程
     *
     * @param studentId
     * @param semesterId
     * @return
     */
    ResponseModel<List<PortraitStudyCourseVO>> getStudyCourse(Long studentId, Long semesterId);
}
