package com.poho.stuup.service;

import com.poho.stuup.model.vo.GrowthReportVO;

import java.util.List;

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
     * 技能与学习素养-职业资格证书
     */
    List<String> SkillsAndLearningLiteracy_ProfessionalQualifications(Integer studentId);

    /**
     * 技能与学习素养-职业资格证书
     */
    List<String> SkillsAndLearningLiteracy_Professionalism(Integer studentId);

    /**
     * 技能与学习素养-双创比赛
     */
    GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition SkillsAndLearningLiteracy_DoubleInnovationCompetition(Integer studentId);

    /**
     * 运动与身心健康-心理素养
     */
    GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PsychologicalLiteracy ExerciseAndPhysicalAndMentalHealth_PsychologicalLiteracy(Integer studentId);

    /**
     * 运动与身心健康-身体素养
     */
    GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PhysicalLiteracy ExerciseAndPhysicalAndMentalHealth_PhysicalLiteracy(Integer studentId);

    /**
     * 审美与艺术修养-艺术活动
     */
    GrowthReportVO.AestheticAndArtisticAccomplishment.ArtisticActivities AestheticAndArtisticAccomplishment_ArtisticActivities(Integer studentId);

    /**
     * 审美与艺术修养-才艺展示
     */
    GrowthReportVO.AestheticAndArtisticAccomplishment.TalentShow AestheticAndArtisticAccomplishment_TalentShow(Integer studentId);

    /**
     * 审美与艺术修养-艺术社团
     */
    GrowthReportVO.AestheticAndArtisticAccomplishment.ArtSocieties AestheticAndArtisticAccomplishment_ArtSocieties(Integer studentId);

    /**
     * 劳动与职业素养-艺术活动
     */
    GrowthReportVO.LaborAndProfessionalism.ArtisticActivities LaborAndProfessionalism_ArtisticActivities(Integer studentId);

    /**
     * 劳动与职业素养-志原者活动学分
     */
    List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> LaborAndProfessionalism_CreditsForShiharaActivities(Integer studentId);

    /**
     * 劳动与职业素养-生产劳动实践学分
     */
    List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> LaborAndProfessionalism_ProductionLaborPracticeCredits(Integer studentId);

}
