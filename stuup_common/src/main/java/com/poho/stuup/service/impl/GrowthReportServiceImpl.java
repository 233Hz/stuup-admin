package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.vo.GrowthReportVO;
import com.poho.stuup.service.GrowthReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class GrowthReportServiceImpl implements GrowthReportService {

    private final StudentMapper studentMapper;
    private final ClassMapper classMapper;
    private final MajorMapper majorMapper;
    private final GrowthMapper growthMapper;
    private final GrowthItemMapper growthItemMapper;
    private final RecMilitaryMapper recMilitaryMapper;
    private final RecDefaultMapper recDefaultMapper;
    private final SyncCommunityMemberMapper syncCommunityMemberMapper;

    @Override
    public GrowthReportVO.BasicInfo getReportBasicInfo(Integer studentId) {
        Student student = studentMapper.selectByPrimaryKey(studentId);

        Integer sex = student.getSex();
        String gender;
        if (sex == 1) gender = "男";
        else if (sex == 2) gender = "女";
        else gender = "未知";

        String className = null;
        Integer classId = student.getClassId();
        if (classId != null) className = classMapper.selectClassNameById(classId);
        if (className == null) className = "无";

        String majorName = null;
        Integer majorId = student.getMajorId();
        if (majorId != null) majorName = majorMapper.selectMajorNameById(majorId);
        if (majorName == null) majorName = "无";

        String idCard = student.getIdCard();
        if (idCard == null) idCard = "无";

        String phone = student.getPhone();
        if (phone == null) idCard = "无";

        Integer status = student.getStatus();
        String academicStatus;
        if (status == 0) academicStatus = "毕业";
        else if (status == 1) academicStatus = "正常";
        else if (status == 2) academicStatus = "学籍变更";
        else academicStatus = "未知";

        Long studentIdL = Long.valueOf(studentId);
        String militaryTrainingLevel = "未知";
        List<RecMilitary> recMilitaries = recMilitaryMapper.selectList(Wrappers.<RecMilitary>lambdaQuery()
                .select(RecMilitary::getLevel)
                .eq(RecMilitary::getStudentId, studentIdL)
                .orderByDesc(RecMilitary::getCreateTime)
                .last("limit 1"));
        if (recMilitaries != null && !recMilitaries.isEmpty()) {
            RecMilitary recMilitary = recMilitaries.get(0);
            Integer excellent = recMilitary.getLevel();
            if (excellent == 1) militaryTrainingLevel = "合格";
            else if (excellent == 2) militaryTrainingLevel = "不合格";
        }

        return GrowthReportVO.BasicInfo.builder()
                .studentId(studentId)
                .studentNo(student.getStudentNo())
                .studentName(student.getName())
                .gender(gender)
                .ethnicGroup("无")
                .className(className)
                .majorName(majorName)
                .homeAddress("无")
                .dateOfBirth("无")
                .politicalStatus("无")
                .idCard(idCard)
                .phone(phone)
                .academicStatus(academicStatus)
                .militaryTrainingLevel(militaryTrainingLevel)
                .build();

    }

    @Override
    public GrowthReportVO.EthicsAndCitizenship getReportEthicsAndCitizenship(Integer studentId) {
        Student student = studentMapper.selectByPrimaryKey(studentId);
        GrowthReportVO.EthicsAndCitizenship.IdeologicalCharacter ideologicalCharacter = EthicsAndCitizenship_IdeologicalCharacter(studentId, student.getStudentNo());
        GrowthReportVO.EthicsAndCitizenship.CivilizedCultivation civilizedCultivation = EthicsAndCitizenship_CivilizedCultivation(studentId);
        GrowthReportVO.EthicsAndCitizenship.BeDisciplinedAndSelfDisciplined beDisciplinedAndSelfDisciplined = EthicsAndCitizenship_BeDisciplinedAndSelfDisciplined(studentId);
        GrowthReportVO.EthicsAndCitizenship.IndividualHonors individualHonors = EthicsAndCitizenship_IndividualHonors(studentId);
        return GrowthReportVO.EthicsAndCitizenship
                .builder()
                .ideologicalCharacter(ideologicalCharacter)
                .civilizedCultivation(civilizedCultivation)
                .beDisciplinedAndSelfDisciplined(beDisciplinedAndSelfDisciplined)
                .individualHonors(individualHonors)
                .build();
    }

    @Override
    public GrowthReportVO.SkillsAndLearningLiteracy getReportSkillsAndLearningLiteracy(Integer studentId) {

        GrowthReportVO.SkillsAndLearningLiteracy.SubjectGrades subjectGrades = SkillsAndLearningLiteracy_SubjectGrades(studentId);
        GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition doubleInnovationCompetition = SkillsAndLearningLiteracy_DoubleInnovationCompetition(studentId);

        return GrowthReportVO.SkillsAndLearningLiteracy
                .builder()
                .subjectGrades(subjectGrades)
                .doubleInnovationCompetition(doubleInnovationCompetition)
                .build();
    }

    @Override
    public GrowthReportVO.ExerciseAndPhysicalAndMentalHealth getReportExerciseAndPhysicalAndMentalHealth(Integer studentId) {
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getCode)
                .in(GrowthItem::getCode, Arrays.asList("CZ_052", "CZ_053")));
        Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

        long countPsychodramaOrPsychoMonthShowcase = 0, countPsychologicalCenter = 0;

        Long psychodramaOrPsychoMonthShowcaseGrowthId = growthItemMap.get("CZ_052");
        if (psychodramaOrPsychoMonthShowcaseGrowthId != null) {
            countPsychodramaOrPsychoMonthShowcase = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, countPsychodramaOrPsychoMonthShowcase));
        }

        Long psychologicalCenterGrowthId = growthItemMap.get("CZ_053");
        if (psychologicalCenterGrowthId != null) {
            countPsychologicalCenter = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, psychologicalCenterGrowthId));
        }

        return GrowthReportVO.ExerciseAndPhysicalAndMentalHealth
                .builder()
                .countPsychodramaOrPsychoMonthShowcase(countPsychodramaOrPsychoMonthShowcase)
                .countPsychologicalCenter(countPsychologicalCenter)
                .build();
    }

    @Override
    public GrowthReportVO.AestheticAndArtisticAccomplishment getReportAestheticAndArtisticAccomplishment(Integer studentId) {
        return null;
    }

    @Override
    public GrowthReportVO.LaborAndProfessionalism getReportLaborAndProfessionalism(Integer studentId) {
        return null;
    }

    @Override
    public GrowthReportVO.EthicsAndCitizenship.IdeologicalCharacter EthicsAndCitizenship_IdeologicalCharacter(Integer studentId, String studentNo) {
        List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getName)
                .in(Growth::getName, Arrays.asList("爱国爱校", "时政学习", "安全法制")));
        Map<String, Long> growthMap = growths.stream().collect(Collectors.toMap(Growth::getName, Growth::getId));
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getThreeLevelId, GrowthItem::getId)
                .in(GrowthItem::getThreeLevelId, growthMap.values()));
        Map<String, List<Long>> map = new HashMap<>();
        for (Map.Entry<String, Long> entry : growthMap.entrySet()) {
            Long growthId = entry.getValue();
            List<Long> growthItemId = growthItems.stream().filter(growthItem -> growthItem.getThreeLevelId().equals(growthId))
                    .map(GrowthItem::getId)
                    .collect(Collectors.toList());
            map.put(entry.getKey(), growthItemId);
        }
        long countLoveTheCountryAndTheSchool = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId, map.getOrDefault("爱国爱校", Collections.emptyList())));

        long countCurrentPoliticsStudy = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId, map.getOrDefault("时政学习", Collections.emptyList())));
        long countSecurityRuleOfLaw = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId, map.getOrDefault("安全法制", Collections.emptyList())));

        List<String> participatingSocieties = syncCommunityMemberMapper.selectList(Wrappers.<SyncCommunityMember>lambdaQuery()
                        .select(SyncCommunityMember::getCommunityName)
                        .eq(SyncCommunityMember::getStuNo, studentNo)
                        .eq(SyncCommunityMember::getState, 1)
                        .isNotNull(SyncCommunityMember::getCommunityName))
                .stream().map(SyncCommunityMember::getCommunityName)
                .collect(Collectors.toList());

        return GrowthReportVO.EthicsAndCitizenship.IdeologicalCharacter
                .builder()
                .countArtSocieties(0L)
                .countLoveTheCountryAndTheSchool(countLoveTheCountryAndTheSchool)
                .countCurrentPoliticsStudy(countCurrentPoliticsStudy)
                .countSecurityRuleOfLaw(countSecurityRuleOfLaw)
                .participatingSocieties(participatingSocieties)
                .build();
    }

    @Override
    public GrowthReportVO.EthicsAndCitizenship.CivilizedCultivation EthicsAndCitizenship_CivilizedCultivation(Integer studentId) {
        List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getName)
                .in(Growth::getName, Arrays.asList("六项评比（流动红旗）", "文明寝室", "安全法制", "仪表规范", "仪容整洁", "文明礼仪", "卫生环境")));
        Map<String, Long> growthMap = growths.stream().collect(Collectors.toMap(Growth::getName, Growth::getId));
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getThreeLevelId, GrowthItem::getId)
                .in(GrowthItem::getThreeLevelId, growthMap.values()));
        Map<String, List<Long>> map = new HashMap<>();
        for (Map.Entry<String, Long> entry : growthMap.entrySet()) {
            Long growthId = entry.getValue();
            List<Long> growthItemId = growthItems.stream().filter(growthItem -> growthItem.getThreeLevelId().equals(growthId))
                    .map(GrowthItem::getId)
                    .collect(Collectors.toList());
            map.put(entry.getKey(), growthItemId);
        }

        long countMobileRedFlags = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId, map.getOrDefault("六项评比（流动红旗）", Collections.emptyList())));

        long countCivilizationBedroom = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId, map.getOrDefault("文明寝室", Collections.emptyList())));

        long countGroomingViolations = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId,
                        Stream.of(map.getOrDefault("仪容整洁", Collections.emptyList()),
                                        map.getOrDefault("仪表规范", Collections.emptyList()))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList())
                ));

        long countViolationOfCivilityAndEtiquette = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId, map.getOrDefault("文明礼仪", Collections.emptyList())));

        long countSanitaryEnvironmentViolations = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                .eq(RecDefault::getStudentId, studentId)
                .in(RecDefault::getGrowId, map.getOrDefault("卫生环境", Collections.emptyList())));


        return GrowthReportVO.EthicsAndCitizenship.CivilizedCultivation
                .builder()
                .countMobileRedFlags(countMobileRedFlags)
                .countCivilizationBedroom(countCivilizationBedroom)
                .countGroomingViolations(countGroomingViolations)
                .countViolationOfCivilityAndEtiquette(countViolationOfCivilityAndEtiquette)
                .countSanitaryEnvironmentViolations(countSanitaryEnvironmentViolations)
                .build();
    }

    @Override
    public GrowthReportVO.EthicsAndCitizenship.BeDisciplinedAndSelfDisciplined EthicsAndCitizenship_BeDisciplinedAndSelfDisciplined(Integer studentId) {
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getCode)
                .in(GrowthItem::getCode, Arrays.asList("CZ_012", "CZ_013", "CZ_014", "CZ_015")));
        Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

        long countDisciplinaryAction = 0, countLateArrivalAndEarlyDeparture = 0, countAbsenteeismFromClass = 0, countNotGettingOutOnTime = 0;

        Long disciplinaryActionGrowthId = growthItemMap.get("CZ_012");
        if (disciplinaryActionGrowthId != null) {
            countDisciplinaryAction = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, disciplinaryActionGrowthId));
        }

        Long lateArrivalAndEarlyDepartureGrowthId = growthItemMap.get("CZ_013");
        if (lateArrivalAndEarlyDepartureGrowthId != null) {
            countLateArrivalAndEarlyDeparture = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, lateArrivalAndEarlyDepartureGrowthId));
        }

        Long absenteeismFromClassGrowthId = growthItemMap.get("CZ_014");
        if (absenteeismFromClassGrowthId != null) {
            countAbsenteeismFromClass = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, absenteeismFromClassGrowthId));
        }

        Long notGettingOutOnTimeGrowthId = growthItemMap.get("CZ_015");
        if (notGettingOutOnTimeGrowthId != null) {
            countNotGettingOutOnTime = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, notGettingOutOnTimeGrowthId));
        }

        return GrowthReportVO.EthicsAndCitizenship.BeDisciplinedAndSelfDisciplined
                .builder()
                .countDisciplinaryAction(countDisciplinaryAction)
                .countLateArrivalAndEarlyDeparture(countLateArrivalAndEarlyDeparture)
                .countAbsenteeismFromClass(countAbsenteeismFromClass)
                .countNotGettingOutOnTime(countNotGettingOutOnTime)
                .build();
    }

    @Override
    public GrowthReportVO.EthicsAndCitizenship.IndividualHonors EthicsAndCitizenship_IndividualHonors(Integer studentId) {
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getCode)
                .in(GrowthItem::getCode, Arrays.asList("CZ_028", "CZ_029", "CZ_030", "CZ_031")));
        Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

        long countNationalScholarships = 0, countMunicipalHonors = 0, countDistrictLevelIndustryHonors = 0, countSchoolScholarships = 0;

        Long nationalScholarshipsGrowthId = growthItemMap.get("CZ_028");
        if (nationalScholarshipsGrowthId != null) {
            countNationalScholarships = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, nationalScholarshipsGrowthId));
        }

        Long municipalHonorsGrowthId = growthItemMap.get("CZ_013");
        if (municipalHonorsGrowthId != null) {
            countMunicipalHonors = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, municipalHonorsGrowthId));
        }

        Long districtLevelIndustryHonorsGrowthId = growthItemMap.get("CZ_014");
        if (districtLevelIndustryHonorsGrowthId != null) {
            countDistrictLevelIndustryHonors = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, districtLevelIndustryHonorsGrowthId));
        }

        Long schoolScholarshipsGrowthId = growthItemMap.get("CZ_015");
        if (schoolScholarshipsGrowthId != null) {
            countSchoolScholarships = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, schoolScholarshipsGrowthId));
        }

        return GrowthReportVO.EthicsAndCitizenship.IndividualHonors
                .builder()
                .countNationalScholarships(countNationalScholarships)
                .countMunicipalHonors(countMunicipalHonors)
                .countDistrictLevelIndustryHonors(countDistrictLevelIndustryHonors)
                .countSchoolScholarships(countSchoolScholarships)
                .build();
    }

    @Override
    public GrowthReportVO.SkillsAndLearningLiteracy.SubjectGrades SkillsAndLearningLiteracy_SubjectGrades(Integer studentId) {
        return GrowthReportVO.SkillsAndLearningLiteracy.SubjectGrades.builder().build();
    }

    @Override
    public GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition SkillsAndLearningLiteracy_DoubleInnovationCompetition(Integer studentId) {
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getCode)
                .in(GrowthItem::getCode, Arrays.asList("CZ_041", "CZ_042", "CZ_043", "CZ_044")));
        Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

        long countNationalLevel = 0, countMunicipalLevel = 0, countDistrictIndustryLevel = 0, countSchoolLevel = 0;

        Long nationalLevelGrowthId = growthItemMap.get("CZ_041");
        if (nationalLevelGrowthId != null) {
            countNationalLevel = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, nationalLevelGrowthId));
        }

        Long municipalLevelGrowthId = growthItemMap.get("CZ_042");
        if (municipalLevelGrowthId != null) {
            countMunicipalLevel = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, municipalLevelGrowthId));
        }

        Long districtIndustryLevelGrowthId = growthItemMap.get("CZ_043");
        if (districtIndustryLevelGrowthId != null) {
            countDistrictIndustryLevel = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, districtIndustryLevelGrowthId));
        }

        Long schoolLevelGrowthId = growthItemMap.get("CZ_044");
        if (schoolLevelGrowthId != null) {
            countSchoolLevel = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, schoolLevelGrowthId));
        }


        return GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition
                .builder()
                .countNationalLevel(countNationalLevel)
                .countMunicipalLevel(countMunicipalLevel)
                .countDistrictIndustryLevel(countDistrictIndustryLevel)
                .countSchoolLevel(countSchoolLevel)
                .build();
    }

    public GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PhysicalLiteracy ExerciseAndPhysicalAndMentalHealth_PhysicalLiteracy(Integer studentId) {
        List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getCode)
                .in(GrowthItem::getCode, Arrays.asList("CZ_012", "CZ_013", "CZ_014", "CZ_015")));
        Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

        long countDisciplinaryAction = 0, countLateArrivalAndEarlyDeparture = 0, countAbsenteeismFromClass = 0, countNotGettingOutOnTime = 0;

        Long disciplinaryActionGrowthId = growthItemMap.get("CZ_012");
        if (disciplinaryActionGrowthId != null) {
            countDisciplinaryAction = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, disciplinaryActionGrowthId));
        }

        Long lateArrivalAndEarlyDepartureGrowthId = growthItemMap.get("CZ_013");
        if (lateArrivalAndEarlyDepartureGrowthId != null) {
            countLateArrivalAndEarlyDeparture = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, lateArrivalAndEarlyDepartureGrowthId));
        }

        Long absenteeismFromClassGrowthId = growthItemMap.get("CZ_014");
        if (absenteeismFromClassGrowthId != null) {
            countAbsenteeismFromClass = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, absenteeismFromClassGrowthId));
        }

        Long notGettingOutOnTimeGrowthId = growthItemMap.get("CZ_015");
        if (notGettingOutOnTimeGrowthId != null) {
            countNotGettingOutOnTime = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                    .eq(RecDefault::getStudentId, studentId)
                    .eq(RecDefault::getGrowId, notGettingOutOnTimeGrowthId));
        }
        return GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PhysicalLiteracy
                .builder()
                .build();
    }

}
