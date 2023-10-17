package com.poho.stuup.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.vo.GrowthReportVO;
import com.poho.stuup.service.GrowthReportService;
import com.poho.stuup.service.SemesterService;
import com.poho.stuup.service.manager.GrowthReportManger;
import com.poho.stuup.util.MinioUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GrowthReportServiceImpl implements GrowthReportService {

    private final GrowthReportManger growthReportManger;
    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final ClassMapper classMapper;
    private final MajorMapper majorMapper;
    private final GrowthItemMapper growthItemMapper;
    private final RecMilitaryMapper recMilitaryMapper;
    private final RecDefaultMapper recDefaultMapper;
    private final RecProjectMapper recProjectMapper;
    private final SyncCommunityMemberMapper syncCommunityMemberMapper;
    private final SemesterService semesterService;
    private final FileMapper fileMapper;
    private final RecAddScoreMapper recAddScoreMapper;

    /**
     * 基本信息
     */
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

        AtomicReference<String> username = new AtomicReference<>();
        AtomicReference<String> avatarUrl = new AtomicReference<>();
        Optional.ofNullable(StpUtil.getLoginId().toString())
                .flatMap(userId -> Optional.ofNullable(userMapper.selectByPrimaryKey(Long.valueOf(userId)))
                        .flatMap(user -> {
                            username.set(user.getUserName());
                            return Optional.ofNullable(user.getAvatarId());
                        }))
                .flatMap(avatarId -> Optional.ofNullable(fileMapper.selectById(avatarId)))
                .flatMap(file -> {
                    try {
                        return Optional.ofNullable(MinioUtils.getPreSignedObjectUrl(file.getBucket(), file.getStorageName()));
                    } catch (Exception e) {
                        log.error("{}用户头像获取失败", username.get(), e);
                        return Optional.empty();
                    }
                })
                .ifPresent(avatarUrl::set);

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
                .avatarUrl(avatarUrl.get())
                .build();

    }

    /**
     * 道德与公民素养
     */
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

    /**
     * 技能与学习素养
     */
    @Override
    public GrowthReportVO.SkillsAndLearningLiteracy getReportSkillsAndLearningLiteracy(Integer studentId) {
        GrowthReportVO.SkillsAndLearningLiteracy.SubjectGrades subjectGrades = SkillsAndLearningLiteracy_SubjectGrades(studentId);
        List<String> professionalQualifications = SkillsAndLearningLiteracy_ProfessionalQualifications(studentId);
        List<String> professionalism = SkillsAndLearningLiteracy_Professionalism(studentId);
        GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition doubleInnovationCompetition = SkillsAndLearningLiteracy_DoubleInnovationCompetition(studentId);
        return GrowthReportVO.SkillsAndLearningLiteracy
                .builder()
                .subjectGrades(subjectGrades)
                .professionalQualifications(professionalQualifications)
                .professionalism(professionalism)
                .doubleInnovationCompetition(doubleInnovationCompetition)
                .build();
    }

    /**
     * 运动与身心健康
     */
    @Override
    public GrowthReportVO.ExerciseAndPhysicalAndMentalHealth getReportExerciseAndPhysicalAndMentalHealth(Integer studentId) {
        GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PsychologicalLiteracy psychologicalLiteracy = ExerciseAndPhysicalAndMentalHealth_PsychologicalLiteracy(studentId);
        GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PhysicalLiteracy physicalLiteracy = ExerciseAndPhysicalAndMentalHealth_PhysicalLiteracy(studentId);
        return GrowthReportVO.ExerciseAndPhysicalAndMentalHealth
                .builder()
                .psychologicalLiteracy(psychologicalLiteracy)
                .physicalLiteracy(physicalLiteracy)
                .build();
    }

    /**
     * 审美与艺术修养
     */
    @Override
    public GrowthReportVO.AestheticAndArtisticAccomplishment getReportAestheticAndArtisticAccomplishment(Integer studentId) {
        GrowthReportVO.AestheticAndArtisticAccomplishment.ArtisticActivities artisticActivities = AestheticAndArtisticAccomplishment_ArtisticActivities(studentId);
        GrowthReportVO.AestheticAndArtisticAccomplishment.TalentShow talentShow = AestheticAndArtisticAccomplishment_TalentShow(studentId);
        GrowthReportVO.AestheticAndArtisticAccomplishment.ArtSocieties artSocieties = AestheticAndArtisticAccomplishment_ArtSocieties(studentId);
        return GrowthReportVO.AestheticAndArtisticAccomplishment
                .builder()
                .artisticActivities(artisticActivities)
                .talentShow(talentShow)
                .artSocieties(artSocieties)
                .build();
    }

    /**
     * 社会实践与志愿服务
     */
    @Override
    public GrowthReportVO.LaborAndProfessionalism getReportLaborAndProfessionalism(Integer studentId) {
        GrowthReportVO.LaborAndProfessionalism.ArtisticActivities artisticActivities = LaborAndProfessionalism_ArtisticActivities(studentId);
        List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> creditCompletions = LaborAndProfessionalism_CreditsForShiharaActivities(studentId);
        List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> creditCompletions1 = LaborAndProfessionalism_ProductionLaborPracticeCredits(studentId);
        return GrowthReportVO.LaborAndProfessionalism
                .builder()
                .artisticActivities(artisticActivities)
                .creditsForShiharaActivities(creditCompletions)
                .productionLaborPracticeCredits(creditCompletions1)
                .build();
    }

    /**
     * 道德与公民素养-思想品德
     */
    @Override
    public GrowthReportVO.EthicsAndCitizenship.IdeologicalCharacter EthicsAndCitizenship_IdeologicalCharacter(Integer studentId, String studentNo) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map ethicsAndCitizenshipConfig = (Map) config.get("ethicsAndCitizenship");

        AtomicLong countLoveTheCountryAndTheSchool = new AtomicLong();  // 爱国爱校
        AtomicLong countCurrentPoliticsStudy = new AtomicLong();        // 时政学习
        AtomicLong countSecurityRuleOfLaw = new AtomicLong();           // 安全法制

        if (ethicsAndCitizenshipConfig != null && !ethicsAndCitizenshipConfig.isEmpty()) {
            Map<String, List<String>> ideologicalCharacterConfig = (Map<String, List<String>>) ethicsAndCitizenshipConfig.get("ideologicalCharacter");
            if (ideologicalCharacterConfig != null && !ideologicalCharacterConfig.isEmpty()) {
                Map<String, AtomicLong> map = new HashMap<>();
                map.put("loveTheCountryAndTheSchool", countLoveTheCountryAndTheSchool);
                map.put("currentPoliticsStudy", countCurrentPoliticsStudy);
                map.put("securityRuleOfLaw", countSecurityRuleOfLaw);

                for (Map.Entry<String, AtomicLong> entry : map.entrySet()) {
                    Optional.ofNullable(ideologicalCharacterConfig.get(entry.getKey()))
                            .flatMap(codes -> {
                                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                                        .select(GrowthItem::getId)
                                        .in(GrowthItem::getCode, codes));
                                return Optional.ofNullable(growthItems);
                            })
                            .map(growthItems -> growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList()))
                            .flatMap(ids -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .in(RecDefault::getGrowId, ids));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }

            }
        }

        List<String> participatingSocieties = syncCommunityMemberMapper.selectList(Wrappers.<SyncCommunityMember>lambdaQuery()
                        .select(SyncCommunityMember::getCommunityName)
                        .eq(SyncCommunityMember::getStuNo, studentNo)
                        .eq(SyncCommunityMember::getState, 1)
                        .isNotNull(SyncCommunityMember::getCommunityName))
                .stream().map(SyncCommunityMember::getCommunityName)
                .collect(Collectors.toList());

        BigDecimal countIdeologicalCharacterTotalScore = EthicsAndCitizenship_IdeologicalCharacterTotalScore(studentId);


        return GrowthReportVO.EthicsAndCitizenship.IdeologicalCharacter
                .builder()
                .countLoveTheCountryAndTheSchool(countLoveTheCountryAndTheSchool.get())
                .countCurrentPoliticsStudy(countCurrentPoliticsStudy.get())
                .countSecurityRuleOfLaw(countSecurityRuleOfLaw.get())
                .participatingSocieties(participatingSocieties)
                .countIdeologicalCharacterTotalScore(countIdeologicalCharacterTotalScore)
                .build();
    }

    /**
     * 道德与公民素养-文明修养
     */
    @Override
    public GrowthReportVO.EthicsAndCitizenship.CivilizedCultivation EthicsAndCitizenship_CivilizedCultivation(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map ethicsAndCitizenshipConfig = (Map) config.get("ethicsAndCitizenship");

        AtomicLong countMobileRedFlags = new AtomicLong();                  // 六项评比（流动红旗）
        AtomicLong countCivilizationBedroom = new AtomicLong();             // 文明寝室
        AtomicLong countGroomingViolations = new AtomicLong();              // 仪表仪容违规
        AtomicLong countViolationOfCivilityAndEtiquette = new AtomicLong(); // 文明礼仪违规
        AtomicLong countSanitaryEnvironmentViolations = new AtomicLong();   // 卫生环境违规


        if (ethicsAndCitizenshipConfig != null && !ethicsAndCitizenshipConfig.isEmpty()) {
            Map<String, List<String>> civilizedCultivationConfig = (Map<String, List<String>>) ethicsAndCitizenshipConfig.get("civilizedCultivation");
            if (civilizedCultivationConfig != null && !civilizedCultivationConfig.isEmpty()) {
                Map<String, AtomicLong> map = new HashMap<>();
                map.put("mobileRedFlags", countMobileRedFlags);
                map.put("civilizationBedroom", countCivilizationBedroom);
                map.put("groomingViolations", countGroomingViolations);
                map.put("violationOfCivilityAndEtiquette", countViolationOfCivilityAndEtiquette);
                map.put("sanitaryEnvironmentViolations", countSanitaryEnvironmentViolations);


                for (Map.Entry<String, AtomicLong> entry : map.entrySet()) {
                    Optional.ofNullable(civilizedCultivationConfig.get(entry.getKey()))
                            .flatMap(codes -> {
                                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                                        .select(GrowthItem::getId)
                                        .in(GrowthItem::getCode, codes));
                                return Optional.ofNullable(growthItems);
                            })
                            .map(growthItems -> growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList()))
                            .flatMap(ids -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .in(RecDefault::getGrowId, ids));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }


        return GrowthReportVO.EthicsAndCitizenship.CivilizedCultivation
                .builder()
                .countMobileRedFlags(countMobileRedFlags.get())
                .countCivilizationBedroom(countCivilizationBedroom.get())
                .countGroomingViolations(countGroomingViolations.get())
                .countViolationOfCivilityAndEtiquette(countViolationOfCivilityAndEtiquette.get())
                .countSanitaryEnvironmentViolations(countSanitaryEnvironmentViolations.get())
                .build();
    }

    /**
     * 道德与公民素养-遵纪自律
     */
    @Override
    public GrowthReportVO.EthicsAndCitizenship.BeDisciplinedAndSelfDisciplined EthicsAndCitizenship_BeDisciplinedAndSelfDisciplined(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map ethicsAndCitizenshipConfig = (Map) config.get("ethicsAndCitizenship");

        AtomicLong countDisciplinaryAction = new AtomicLong();  // 纪律处分
        AtomicLong countLateArrivalAndEarlyDeparture = new AtomicLong();  // 迟到早退
        AtomicLong countAbsenteeismFromClass = new AtomicLong();  // 上课缺勤
        AtomicLong countNotGettingOutOnTime = new AtomicLong();  // 不准时出操

        if (ethicsAndCitizenshipConfig != null && !ethicsAndCitizenshipConfig.isEmpty()) {
            Map<String, String> beDisciplinedAndSelfDisciplinedConfig = (Map<String, String>) ethicsAndCitizenshipConfig.get("beDisciplinedAndSelfDisciplined");
            if (beDisciplinedAndSelfDisciplinedConfig != null && !beDisciplinedAndSelfDisciplinedConfig.isEmpty()) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getCode)
                        .in(GrowthItem::getCode, beDisciplinedAndSelfDisciplinedConfig.values()));
                Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

                Map<String, AtomicLong> map1 = new HashMap<>();
                map1.put("disciplinaryAction", countDisciplinaryAction);
                map1.put("lateArrivalAndEarlyDeparture", countLateArrivalAndEarlyDeparture);
                map1.put("absenteeismFromClass", countAbsenteeismFromClass);
                map1.put("notGettingOutOnTime", countNotGettingOutOnTime);

                for (Map.Entry<String, AtomicLong> entry : map1.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, growId));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }

        return GrowthReportVO.EthicsAndCitizenship.BeDisciplinedAndSelfDisciplined
                .builder()
                .countDisciplinaryAction(countDisciplinaryAction.get())
                .countLateArrivalAndEarlyDeparture(countLateArrivalAndEarlyDeparture.get())
                .countAbsenteeismFromClass(countAbsenteeismFromClass.get())
                .countNotGettingOutOnTime(countNotGettingOutOnTime.get())
                .build();
    }

    @Override
    public BigDecimal EthicsAndCitizenship_IdeologicalCharacterTotalScore(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map ethicsAndCitizenshipConfig = (Map) config.get("ethicsAndCitizenship");

        AtomicReference<BigDecimal> totalScore = new AtomicReference<>(BigDecimal.ZERO);
        if (ethicsAndCitizenshipConfig != null && !ethicsAndCitizenshipConfig.isEmpty()) {
            List<String> recCodes = new ArrayList<>();
            Map<String, Object> ideologicalCharacterConfig = (Map<String, Object>) ethicsAndCitizenshipConfig.get("ideologicalCharacter");
            if (ideologicalCharacterConfig != null && !ideologicalCharacterConfig.isEmpty()) {
                ideologicalCharacterConfig.values().forEach(codes -> {
                    if (codes instanceof List) {
                        List<Object> list = (List<Object>) codes;
                        list.forEach(element -> {
                            if (element instanceof String) recCodes.add((String) element);
                        });
                    } else if (codes instanceof String) {
                        recCodes.add((String) codes);
                    }
                });
            }
            if (!recCodes.isEmpty()) {
                List<Long> ids = growthItemMapper.selectObjs(Wrappers.<GrowthItem>lambdaQuery()
                                .select(GrowthItem::getId)
                                .in(GrowthItem::getCode, recCodes))
                        .stream()
                        .map(id -> (Long) id)
                        .collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    recAddScoreMapper.selectObjs(Wrappers.<RecAddScore>lambdaQuery()
                                    .select(RecAddScore::getScore)
                                    .eq(RecAddScore::getStudentId, studentId)
                                    .in(RecAddScore::getGrowId, ids))
                            .stream()
                            .map(score -> (BigDecimal) score)
                            .reduce(BigDecimal::add)
                            .ifPresent(totalScore::set);
                }
            }
        }
        return totalScore.get();
    }

    /**
     * 道德与公民素养-个人荣誉
     */
    @Override
    public GrowthReportVO.EthicsAndCitizenship.IndividualHonors EthicsAndCitizenship_IndividualHonors(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map ethicsAndCitizenshipConfig = (Map) config.get("ethicsAndCitizenship");

        AtomicLong countNationalScholarships = new AtomicLong();        // 国家级奖学金
        AtomicLong countMunicipalHonors = new AtomicLong();             // 市级奖学金
        AtomicLong countDistrictLevelIndustryHonors = new AtomicLong(); // 区（行）级奖学金
        AtomicLong countSchoolScholarships = new AtomicLong();          // 校级奖学金

        if (ethicsAndCitizenshipConfig != null && !ethicsAndCitizenshipConfig.isEmpty()) {
            Map<String, String> individualHonorsConfig = (Map<String, String>) ethicsAndCitizenshipConfig.get("individualHonors");
            if (individualHonorsConfig != null && !individualHonorsConfig.isEmpty()) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getCode)
                        .in(GrowthItem::getCode, individualHonorsConfig.values()));
                Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

                Map<String, AtomicLong> map1 = new HashMap<>();
                map1.put("nationalScholarships", countNationalScholarships);
                map1.put("municipalHonors", countMunicipalHonors);
                map1.put("districtLevelIndustryHonors", countDistrictLevelIndustryHonors);
                map1.put("schoolScholarships", countSchoolScholarships);

                for (Map.Entry<String, AtomicLong> entry : map1.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, growId));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }

        return GrowthReportVO.EthicsAndCitizenship.IndividualHonors
                .builder()
                .countNationalScholarships(countNationalScholarships.get())
                .countMunicipalHonors(countMunicipalHonors.get())
                .countDistrictLevelIndustryHonors(countDistrictLevelIndustryHonors.get())
                .countSchoolScholarships(countSchoolScholarships.get())
                .build();
    }

    /**
     * 技能与学习素养-学科成绩
     */
    @Override
    public GrowthReportVO.SkillsAndLearningLiteracy.SubjectGrades SkillsAndLearningLiteracy_SubjectGrades(Integer studentId) {
        return GrowthReportVO.SkillsAndLearningLiteracy.SubjectGrades.builder().build();
    }

    @Override
    public List<String> SkillsAndLearningLiteracy_ProfessionalQualifications(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map skillsAndLearningLiteracyConfig = (Map) config.get("skillsAndLearningLiteracy");

        List<String> professionalQualifications = new ArrayList<>();

        if (skillsAndLearningLiteracyConfig != null && !skillsAndLearningLiteracyConfig.isEmpty()) {
            String recCode = (String) skillsAndLearningLiteracyConfig.get("professionalQualifications");
            if (StrUtil.isNotBlank(recCode)) {
                GrowthItem growthItem = growthItemMapper.selectOne(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId)
                        .eq(GrowthItem::getCode, recCode));
                Optional.ofNullable(growthItem)
                        .map(GrowthItem::getId)
                        .flatMap(growthItemId -> {
                            List<Object> names = recProjectMapper.selectObjs(Wrappers.<RecProject>lambdaQuery()
                                    .select(RecProject::getName)
                                    .eq(RecProject::getStudentId, studentId)
                                    .eq(RecProject::getGrowId, growthItemId));
                            return Optional.ofNullable(names);
                        })
                        .ifPresent(names -> names.forEach(name -> professionalQualifications.add((String) name)));

            }
        }

        return professionalQualifications;
    }

    @Override
    public List<String> SkillsAndLearningLiteracy_Professionalism(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map skillsAndLearningLiteracyConfig = (Map) config.get("skillsAndLearningLiteracy");

        ArrayList<String> professionalism = new ArrayList<>();
        if (skillsAndLearningLiteracyConfig != null && !skillsAndLearningLiteracyConfig.isEmpty()) {
            List<String> professionalismConfig = (List<String>) skillsAndLearningLiteracyConfig.get("professionalism");
            if (professionalismConfig != null && !professionalismConfig.isEmpty()) {
                List<Object> ids = growthItemMapper.selectObjs(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId)
                        .in(GrowthItem::getCode, professionalismConfig));
                Optional.ofNullable(ids)
                        .map(idList -> idList.stream().map(id -> (Long) id).collect(Collectors.toList()))
                        .flatMap(idList -> {
                            List<Object> names = recProjectMapper.selectObjs(Wrappers.<RecProject>lambdaQuery()
                                    .select(RecProject::getName)
                                    .eq(RecProject::getStudentId, studentId)
                                    .in(RecProject::getGrowId, idList));
                            return Optional.ofNullable(names);
                        })
                        .ifPresent(names -> names.forEach(name -> professionalism.add((String) name)));
            }
        }
        return professionalism;
    }

    /**
     * 技能与学习素养-双创比赛
     */
    @Override
    public GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition SkillsAndLearningLiteracy_DoubleInnovationCompetition(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map skillsAndLearningLiteracyConfig = (Map) config.get("skillsAndLearningLiteracy");

        AtomicLong countNationalLevel = new AtomicLong();                   // 国家级
        AtomicLong countMunicipalLevel = new AtomicLong();                  // 市级
        AtomicLong countDistrictIndustryLevel = new AtomicLong();           // 区（行）级
        AtomicLong countSchoolLevel = new AtomicLong();                     // 校级
        List<String> nationalLevelRecords = new ArrayList<>();              // 国家级记录
        List<String> municipalLevelRecords = new ArrayList<>();             // 市级记录
        List<String> districtIndustryLevelRecords = new ArrayList<>();      // 区（行）级记录
        List<String> schoolLevelRecords = new ArrayList<>();                // 校级记录

        if (skillsAndLearningLiteracyConfig != null && !skillsAndLearningLiteracyConfig.isEmpty()) {
            Map<String, String> doubleInnovationCompetitionConfig = (Map<String, String>) skillsAndLearningLiteracyConfig.get("doubleInnovationCompetition");
            if (doubleInnovationCompetitionConfig != null && !doubleInnovationCompetitionConfig.isEmpty()) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getCode)
                        .in(GrowthItem::getCode, doubleInnovationCompetitionConfig.values()));
                Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

                Map<String, AtomicLong> map1 = new HashMap<>();
                map1.put("nationalLevel", countNationalLevel);
                map1.put("municipalLevel", countMunicipalLevel);
                map1.put("districtIndustryLevel", countDistrictIndustryLevel);
                map1.put("schoolLevel", countSchoolLevel);

                for (Map.Entry<String, AtomicLong> entry : map1.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, growId));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }

                Map<String, List<String>> map2 = new HashMap<>();
                map2.put("nationalLevel", nationalLevelRecords);
                map2.put("municipalLevel", municipalLevelRecords);
                map2.put("districtIndustryLevel", districtIndustryLevelRecords);
                map2.put("schoolLevel", schoolLevelRecords);

                for (Map.Entry<String, List<String>> entry : map2.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                List<RecProject> recProjects = recProjectMapper.selectList(Wrappers.<RecProject>lambdaQuery()
                                        .select(RecProject::getName)
                                        .eq(RecProject::getStudentId, studentId)
                                        .eq(RecProject::getGrowId, growId));
                                return Optional.ofNullable(recProjects);
                            })
                            .ifPresent(recProjects -> recProjects.forEach(recProject -> entry.getValue().add(recProject.getName())));
                }
            }
        }

        return GrowthReportVO.SkillsAndLearningLiteracy.DoubleInnovationCompetition
                .builder()
                .countNationalLevel(countNationalLevel.get())
                .countMunicipalLevel(countMunicipalLevel.get())
                .countDistrictIndustryLevel(countDistrictIndustryLevel.get())
                .countSchoolLevel(countSchoolLevel.get())
                .nationalLevelRecords(nationalLevelRecords)
                .municipalLevelRecords(municipalLevelRecords)
                .districtIndustryLevelRecords(districtIndustryLevelRecords)
                .schoolLevelRecords(schoolLevelRecords)
                .build();
    }

    /**
     * 运动与身心健康-心理素养
     */
    @Override
    public GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PsychologicalLiteracy ExerciseAndPhysicalAndMentalHealth_PsychologicalLiteracy(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map exerciseAndPhysicalAndMentalHealthConfig = (Map) config.get("exerciseAndPhysicalAndMentalHealth");

        AtomicLong countPsychodramaOrPsychoMonthShowcase = new AtomicLong();        // 参加心理剧或心理月展示活动
        AtomicLong countPsychologicalCenter = new AtomicLong();                     // 参与心理中心活动

        if (exerciseAndPhysicalAndMentalHealthConfig != null && !exerciseAndPhysicalAndMentalHealthConfig.isEmpty()) {
            Map<String, String> psychologicalLiteracyConfig = (Map<String, String>) exerciseAndPhysicalAndMentalHealthConfig.get("psychologicalLiteracy");
            if (psychologicalLiteracyConfig != null && !psychologicalLiteracyConfig.isEmpty()) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getCode)
                        .in(GrowthItem::getCode, psychologicalLiteracyConfig.values()));
                Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

                Map<String, AtomicLong> map1 = new HashMap<>();
                map1.put("psychodramaOrPsychoMonthShowcase", countPsychodramaOrPsychoMonthShowcase);
                map1.put("psychologicalCenter", countPsychologicalCenter);

                for (Map.Entry<String, AtomicLong> entry : map1.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, growId));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }

        return GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PsychologicalLiteracy
                .builder()
                .countPsychodramaOrPsychoMonthShowcase(countPsychodramaOrPsychoMonthShowcase.get())
                .countPsychologicalCenter(countPsychologicalCenter.get())
                .build();
    }

    /**
     * 运动与身心健康-身体素养
     */
    public GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PhysicalLiteracy ExerciseAndPhysicalAndMentalHealth_PhysicalLiteracy(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map exerciseAndPhysicalAndMentalHealthConfig = (Map) config.get("exerciseAndPhysicalAndMentalHealth");

        AtomicLong countNationalLevel = new AtomicLong();            // 国家级
        AtomicLong countMunicipalLevel = new AtomicLong();           // 市级
        AtomicLong countDistrictIndustryLevel = new AtomicLong();    // 区（行）级
        AtomicLong countSchoolLevel = new AtomicLong();              // 校级

        if (exerciseAndPhysicalAndMentalHealthConfig != null && !exerciseAndPhysicalAndMentalHealthConfig.isEmpty()) {
            Map<String, String> physicalLiteracyConfig = (Map<String, String>) exerciseAndPhysicalAndMentalHealthConfig.get("physicalLiteracy");
            if (physicalLiteracyConfig != null && !physicalLiteracyConfig.isEmpty()) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getCode)
                        .in(GrowthItem::getCode, physicalLiteracyConfig.values()));
                Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

                Map<String, AtomicLong> map1 = new HashMap<>();
                map1.put("nationalLevel", countNationalLevel);
                map1.put("municipalLevel", countMunicipalLevel);
                map1.put("districtIndustryLevel", countDistrictIndustryLevel);
                map1.put("schoolLevel", countSchoolLevel);

                for (Map.Entry<String, AtomicLong> entry : map1.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, growId));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }

        return GrowthReportVO.ExerciseAndPhysicalAndMentalHealth.PhysicalLiteracy
                .builder()
                .countNationalLevel(countNationalLevel.get())
                .countMunicipalLevel(countMunicipalLevel.get())
                .countDistrictIndustryLevel(countDistrictIndustryLevel.get())
                .countSchoolLevel(countSchoolLevel.get())
                .build();
    }

    /**
     * 审美与艺术修养-艺术活动
     */
    @Override
    public GrowthReportVO.AestheticAndArtisticAccomplishment.ArtisticActivities AestheticAndArtisticAccomplishment_ArtisticActivities(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map aestheticAndArtisticAccomplishment = (Map) config.get("aestheticAndArtisticAccomplishment");

        AtomicLong countTalentActivities = new AtomicLong();  // 参加艺术活动

        if (aestheticAndArtisticAccomplishment != null && !aestheticAndArtisticAccomplishment.isEmpty()) {
            Map<String, List<String>> artisticActivitiesConfig = (Map<String, List<String>>) aestheticAndArtisticAccomplishment.get("artisticActivities");
            if (artisticActivitiesConfig != null && !artisticActivitiesConfig.isEmpty()) {
                Map<String, AtomicLong> map = new HashMap<>();
                map.put("talentActivities", countTalentActivities);

                for (Map.Entry<String, AtomicLong> entry : map.entrySet()) {
                    Optional.ofNullable(artisticActivitiesConfig.get(entry.getKey()))
                            .flatMap(codes -> {
                                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                                        .select(GrowthItem::getId)
                                        .in(GrowthItem::getCode, codes));
                                return Optional.ofNullable(growthItems);
                            })
                            .map(growthItems -> growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList()))
                            .flatMap(growId -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, growId));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }
        return GrowthReportVO.AestheticAndArtisticAccomplishment.ArtisticActivities
                .builder()
                .countTalentActivities(countTalentActivities.get())
                .build();
    }

    /**
     * 审美与艺术修养-才艺展示
     */
    @Override
    public GrowthReportVO.AestheticAndArtisticAccomplishment.TalentShow AestheticAndArtisticAccomplishment_TalentShow(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map aestheticAndArtisticAccomplishmentConfig = (Map) config.get("aestheticAndArtisticAccomplishment");

        AtomicLong countNationalLevel = new AtomicLong();                   // 国家级
        AtomicLong countMunicipalLevel = new AtomicLong();                  // 市级
        AtomicLong countDistrictIndustryLevel = new AtomicLong();           // 区（行）级
        AtomicLong countSchoolLevel = new AtomicLong();                     // 校级
        List<String> nationalLevelRecords = new ArrayList<>();              // 国家级记录
        List<String> municipalLevelRecords = new ArrayList<>();             // 市级记录
        List<String> districtIndustryLevelRecords = new ArrayList<>();      // 区（行）级记录
        List<String> schoolLevelRecords = new ArrayList<>();                // 校级记录

        if (aestheticAndArtisticAccomplishmentConfig != null && !aestheticAndArtisticAccomplishmentConfig.isEmpty()) {
            Map<String, String> talentShowConfig = (Map<String, String>) aestheticAndArtisticAccomplishmentConfig.get("talentShow");
            if (talentShowConfig != null && !talentShowConfig.isEmpty()) {
                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getCode)
                        .in(GrowthItem::getCode, talentShowConfig.values()));
                Map<String, Long> growthItemMap = growthItems.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));

                Map<String, AtomicLong> map1 = new HashMap<>();
                map1.put("nationalLevel", countNationalLevel);
                map1.put("municipalLevel", countMunicipalLevel);
                map1.put("districtIndustryLevel", countDistrictIndustryLevel);
                map1.put("schoolLevel", countSchoolLevel);

                for (Map.Entry<String, AtomicLong> entry : map1.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, growId));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }

                Map<String, List<String>> map2 = new HashMap<>();
                map2.put("nationalLevel", nationalLevelRecords);
                map2.put("municipalLevel", municipalLevelRecords);
                map2.put("districtIndustryLevel", districtIndustryLevelRecords);
                map2.put("schoolLevel", schoolLevelRecords);

                for (Map.Entry<String, List<String>> entry : map2.entrySet()) {
                    Optional.ofNullable(entry.getKey())
                            .map(growthItemMap::get)
                            .flatMap(growId -> {
                                List<RecProject> recProjects = recProjectMapper.selectList(Wrappers.<RecProject>lambdaQuery()
                                        .select(RecProject::getName)
                                        .eq(RecProject::getStudentId, studentId)
                                        .eq(RecProject::getGrowId, growId));
                                return Optional.ofNullable(recProjects);
                            })
                            .ifPresent(recProjects -> recProjects.forEach(recProject -> entry.getValue().add(recProject.getName())));
                }
            }
        }

        return GrowthReportVO.AestheticAndArtisticAccomplishment.TalentShow
                .builder()
                .countNationalLevel(countNationalLevel.get())
                .countMunicipalLevel(countMunicipalLevel.get())
                .countDistrictIndustryLevel(countDistrictIndustryLevel.get())
                .countSchoolLevel(countSchoolLevel.get())
                .nationalLevelRecords(nationalLevelRecords)
                .municipalLevelRecords(municipalLevelRecords)
                .districtIndustryLevelRecords(districtIndustryLevelRecords)
                .schoolLevelRecords(schoolLevelRecords)
                .build();
    }

    /**
     * 审美与艺术修养-艺术社团
     */
    @Override
    public GrowthReportVO.AestheticAndArtisticAccomplishment.ArtSocieties AestheticAndArtisticAccomplishment_ArtSocieties(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map aestheticAndArtisticAccomplishment = (Map) config.get("aestheticAndArtisticAccomplishment");

        AtomicLong countArtSocieties = new AtomicLong();  // 参加艺术活动

        if (aestheticAndArtisticAccomplishment != null && !aestheticAndArtisticAccomplishment.isEmpty()) {
            Map<String, List<String>> artSocietiesConfig = (Map<String, List<String>>) aestheticAndArtisticAccomplishment.get("artSocieties");
            if (artSocietiesConfig != null && !artSocietiesConfig.isEmpty()) {
                Map<String, AtomicLong> map = new HashMap<>();
                map.put("artSocieties", countArtSocieties);

                for (Map.Entry<String, AtomicLong> entry : map.entrySet()) {
                    Optional.ofNullable(artSocietiesConfig.get(entry.getKey()))
                            .flatMap(codes -> {
                                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                                        .select(GrowthItem::getId)
                                        .in(GrowthItem::getCode, codes));
                                return Optional.ofNullable(growthItems);
                            })
                            .map(growthItems -> growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList()))
                            .flatMap(ids -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, ids));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }
        return GrowthReportVO.AestheticAndArtisticAccomplishment.ArtSocieties
                .builder()
                .countArtSocieties(countArtSocieties.get())
                .build();
    }

    /**
     * 劳动与职业素养-艺术活动
     */
    @Override
    public GrowthReportVO.LaborAndProfessionalism.ArtisticActivities LaborAndProfessionalism_ArtisticActivities(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map laborAndProfessionalismConfig = (Map) config.get("laborAndProfessionalism");

        AtomicLong countVolunteerService = new AtomicLong();            // 参加志愿者服务
        AtomicLong countTrafficPostsAreOnDuty = new AtomicLong();       // 参加交通岗执勤

        if (laborAndProfessionalismConfig != null && !laborAndProfessionalismConfig.isEmpty()) {
            Map<String, List<String>> artisticActivitiesConfig = (Map<String, List<String>>) laborAndProfessionalismConfig.get("artisticActivities");
            if (artisticActivitiesConfig != null && !artisticActivitiesConfig.isEmpty()) {
                Map<String, AtomicLong> map1 = new HashMap<>();
                map1.put("volunteerService", countVolunteerService);
                map1.put("trafficPostsAreOnDuty", countTrafficPostsAreOnDuty);

                for (Map.Entry<String, AtomicLong> entry : map1.entrySet()) {
                    Optional.ofNullable(artisticActivitiesConfig.get(entry.getKey()))
                            .flatMap(codes -> {
                                List<GrowthItem> growthItems = growthItemMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                                        .select(GrowthItem::getId)
                                        .in(GrowthItem::getCode, codes));
                                return Optional.ofNullable(growthItems);
                            })
                            .map(growthItems -> growthItems.stream().map(GrowthItem::getId).collect(Collectors.toList()))
                            .flatMap(ids -> {
                                Long count = recDefaultMapper.selectCount(Wrappers.<RecDefault>lambdaQuery()
                                        .eq(RecDefault::getStudentId, studentId)
                                        .eq(RecDefault::getGrowId, ids));
                                return Optional.ofNullable(count);
                            })
                            .ifPresent(entry.getValue()::set);
                }
            }
        }

        return GrowthReportVO.LaborAndProfessionalism.ArtisticActivities
                .builder()
                .countVolunteerService(countVolunteerService.get())
                .countTrafficPostsAreOnDuty(countTrafficPostsAreOnDuty.get())
                .build();
    }

    /**
     * 劳动与职业素养-志原者活动学分
     */
    @Override
    public List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> LaborAndProfessionalism_CreditsForShiharaActivities(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map laborAndProfessionalismConfig = (Map) config.get("laborAndProfessionalism");

        List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> result = new ArrayList<>();
        if (laborAndProfessionalismConfig != null && !laborAndProfessionalismConfig.isEmpty()) {
            String creditsForShiharaActivitiesConfig = (String) laborAndProfessionalismConfig.get("creditsForShiharaActivities");
            if (StrUtil.isNotBlank(creditsForShiharaActivitiesConfig)) {
                List<Semester> studentSemester = semesterService.getStudentSemester(studentId);
                studentSemester.forEach(semester -> {
                    Long semesterId = semester.getId();
                    boolean exists = recDefaultMapper.exists(Wrappers.<RecDefault>lambdaQuery()
                            .eq(RecDefault::getStudentId, studentId)
                            .eq(RecDefault::getSemesterId, semesterId));
                    GrowthReportVO.LaborAndProfessionalism.CreditCompletion creditCompletion = GrowthReportVO.LaborAndProfessionalism.CreditCompletion
                            .builder()
                            .semesterId(semesterId)
                            .semesterName(semester.getName())
                            .completion(exists ? "已完成" : "未完成")
                            .build();
                    result.add(creditCompletion);
                });
            }
        }
        return result;
    }

    /**
     * 劳动与职业素养-生产劳动实践学分
     */
    @Override
    public List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> LaborAndProfessionalism_ProductionLaborPracticeCredits(Integer studentId) {
        Map config = growthReportManger.getGrowthReportConfig();
        Map laborAndProfessionalismConfig = (Map) config.get("laborAndProfessionalism");

        List<GrowthReportVO.LaborAndProfessionalism.CreditCompletion> result = new ArrayList<>();
        if (laborAndProfessionalismConfig != null && !laborAndProfessionalismConfig.isEmpty()) {
            String productionLaborPracticeCreditsConfig = (String) laborAndProfessionalismConfig.get("productionLaborPracticeCredits");
            if (StrUtil.isNotBlank(productionLaborPracticeCreditsConfig)) {
                List<Semester> studentSemester = semesterService.getStudentSemester(studentId);
                studentSemester.forEach(semester -> {
                    Long semesterId = semester.getId();
                    boolean exists = recDefaultMapper.exists(Wrappers.<RecDefault>lambdaQuery()
                            .eq(RecDefault::getStudentId, studentId)
                            .eq(RecDefault::getSemesterId, semesterId));
                    GrowthReportVO.LaborAndProfessionalism.CreditCompletion creditCompletion = GrowthReportVO.LaborAndProfessionalism.CreditCompletion
                            .builder()
                            .semesterId(semesterId)
                            .semesterName(semester.getName())
                            .completion(exists ? "已完成" : "未完成")
                            .build();
                    result.add(creditCompletion);
                });
            }
        }
        return result;
    }


}
