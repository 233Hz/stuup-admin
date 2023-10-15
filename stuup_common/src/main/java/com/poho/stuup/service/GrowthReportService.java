package com.poho.stuup.service;

import com.poho.stuup.model.vo.GrowthReportVO;

public interface GrowthReportService {

    /**
     * 基本信息
     */
    GrowthReportVO.BasicInfo getReportBasicInfo(Integer studentId);

    /**
     * 道德与公民素养
     */
    GrowthReportVO.EthicsAndCitizenship getReportEthicsAndCitizenship(Integer studentId);

    /**
     * 技能与学习素养
     */
    GrowthReportVO.SkillsAndLearningLiteracy getReportSkillsAndLearningLiteracy(Integer studentId);

    /**
     * 运动与身心健康
     */
    GrowthReportVO.ExerciseAndPhysicalAndMentalHealth getReportExerciseAndPhysicalAndMentalHealth(Integer studentId);

    /**
     * 审美与艺术修养
     */
    GrowthReportVO.AestheticAndArtisticAccomplishment getReportAestheticAndArtisticAccomplishment(Integer studentId);

    /**
     * 社会实践与志愿服务
     */
    GrowthReportVO.LaborAndProfessionalism getReportLaborAndProfessionalism(Integer studentId);

    /**
     * 道德与公民素养-思想品德
     */
    GrowthReportVO.EthicsAndCitizenship.IdeologicalCharacter EthicsAndCitizenship_IdeologicalCharacter(Integer studentId, String studentNo);

    /**
     * 道德与公民素养-文明修养
     */
    GrowthReportVO.EthicsAndCitizenship.CivilizedCultivation EthicsAndCitizenship_CivilizedCultivation(Integer studentId);

    /**
     * 道德与公民素养-遵纪自律
     */
    GrowthReportVO.EthicsAndCitizenship.BeDisciplinedAndSelfDisciplined EthicsAndCitizenship_BeDisciplinedAndSelfDisciplined(Integer studentId);

    /**
     * 道德与公民素养-个人荣誉
     */
    GrowthReportVO.EthicsAndCitizenship.IndividualHonors EthicsAndCitizenship_IndividualHonors(Integer studentId);

    /**
     * 技能与学习素养-学科成绩
     */
    GrowthReportVO.SkillsAndLearningLiteracy.SubjectGrades SkillsAndLearningLiteracy_SubjectGrades(Integer studentId);

    /**
     * 技能与学习素养-双创比赛
     */
    GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition SkillsAndLearningLiteracy_DoubleInnovationCompetition(Integer studentId);

    /**
     * 运动与身心健康-身体素养
     */
    GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PhysicalLiteracy ExerciseAndPhysicalAndMentalHealth_PhysicalLiteracy(Integer studentId);
}
