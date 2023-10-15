package com.poho.stuup.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 学生成长报告
 */

@Data
@Builder
public class GrowthReportVO {

    /**
     * 基本信息
     */
    private BasicInfo basicInfo;

    /**
     * 道德与公民素养
     */
    private EthicsAndCitizenship ethicsAndCitizenship;

    /**
     * 技能与学习素养
     */
    private SkillsAndLearningLiteracy skillsAndLearningLiteracy;

    /**
     * 运动与身心健康
     */
    private ExerciseAndPhysicalAndMentalHealth exerciseAndPhysicalAndMentalHealth;

    /**
     * 审美与艺术修养
     */
    private AestheticAndArtisticAccomplishment aestheticAndArtisticAccomplishment;

    /**
     * 劳动与职业素养
     */
    private LaborAndProfessionalism laborAndProfessionalism;

    @Data
    @Builder
    public static class BasicInfo {
        private Integer studentId;
        private String studentNo;
        private String studentName;
        private String gender;
        private String ethnicGroup;
        private String className;
        private String majorName;
        private String homeAddress;
        private String dateOfBirth;
        private String politicalStatus;
        private String idCard;
        private String phone;
        private String academicStatus;
        private String militaryTrainingLevel;
    }

    @Data
    @Builder
    public static class EthicsAndCitizenship {

        /**
         * 思想品德
         */
        private IdeologicalCharacter ideologicalCharacter;

        /**
         * 文明修养
         */
        private CivilizedCultivation civilizedCultivation;

        /**
         * 遵纪自律
         */
        private BeDisciplinedAndSelfDisciplined beDisciplinedAndSelfDisciplined;

        /**
         * 个人荣誉
         */
        private IndividualHonors individualHonors;

        @Data
        @Builder
        public static class IdeologicalCharacter {

            /**
             * 参加学校及以上组织的艺术社团
             */
            private long countArtSocieties;

            /**
             * 参加爱国爱校相关活动
             */
            private long countLoveTheCountryAndTheSchool;

            /**
             * 参加时政学习相关活动
             */
            private long countCurrentPoliticsStudy;

            /**
             * 参加安全法制相关活动
             */
            private long countSecurityRuleOfLaw;

            /**
             * 参加过的社团
             */
            private List<String> participatingSocieties;
        }

        @Data
        @Builder
        public static class CivilizedCultivation {

            /**
             * 获得六项评比流动红旗（班级前十）
             */
            private long countMobileRedFlags;

            /**
             * 获得文明寝室（先进）
             */
            private long countCivilizationBedroom;

            /**
             * 仪表仪容违规
             */
            private long countGroomingViolations;

            /**
             * 文明礼仪违规
             */
            private long countViolationOfCivilityAndEtiquette;

            /**
             * 卫生环境违规
             */
            private long countSanitaryEnvironmentViolations;
        }

        @Data
        @Builder
        public static class BeDisciplinedAndSelfDisciplined {

            /**
             * 纪律处分
             */
            private long countDisciplinaryAction;

            /**
             * 迟到早退
             */
            private long countLateArrivalAndEarlyDeparture;

            /**
             * 上课缺勤
             */
            private long countAbsenteeismFromClass;

            /**
             * 不准时出操
             */
            private long countNotGettingOutOnTime;

        }

        @Data
        @Builder
        public static class IndividualHonors {

            /**
             * 获得国家级奖学金
             */
            private long countNationalScholarships;

            /**
             * 获得区级（行业）荣誉
             */
            private long countDistrictLevelIndustryHonors;

            /**
             * 优秀学生（团）干部
             */
            private long countOutstandingStudentRegimentCadres;

            /**
             * 获得校级奖学金
             */
            private long countSchoolScholarships;

            /**
             * 获得市级荣誉
             */
            private long countMunicipalHonors;

            /**
             * 三好学生
             */
            private long countMiyoshiStudent;

            /**
             * 药校好学子
             */
            private long countGoodStudentsInPharmacySchool;

        }
    }

    @Data
    @Builder
    public static class SkillsAndLearningLiteracy {

        /**
         * 学科成绩
         */
        private SubjectGrades subjectGrades;

        /**
         * 职业资格证书
         */
        private List<String> professionalQualifications;

        /**
         * 职业素养
         */
        private List<String> professionalism;

        /**
         * 双创比赛
         */
        private DoubleInnovationCompetition doubleInnovationCompetition;

        @Data
        @Builder
        public static class SubjectGrades {

            /**
             * 学期id
             */
            private String semesterId;

            /**
             * 学期名称
             */
            private String semesterName;

            /**
             * 期末总分
             */
            private BigDecimal totalScoreAtTheEndOfThePeriod;

            /**
             * 课程成绩
             */
            private List<CourseGrades> courseGrades;

            @Data
            @Builder
            public static class CourseGrades {

                /**
                 * 课程名称
                 */
                private String courseName;

                /**
                 * 课程分数
                 */
                private BigDecimal score;


            }

        }

        @Data
        @Builder
        public static class DoubleInnovationCompetition {

            /**
             * 国家级
             */
            private long countNationalLevel;

            /**
             * 市级
             */
            private long countMunicipalLevel;

            /**
             * 区（行业）级
             */
            private long countDistrictIndustryLevel;

            /**
             * 校级
             */
            private long countSchoolLevel;

            /**
             * 国家级记录
             */
            private List<String> countNationalLevelRecords;

            /**
             * 市级记录
             */
            private List<String> countMunicipalLevelRecords;

            /**
             * 区（行业）级记录
             */
            private List<String> countDistrictIndustryLevelRecords;

            /**
             * 校级记录
             */
            private List<String> schoolLevelRecords;

        }

    }

    @Data
    @Builder
    public static class ExerciseAndPhysicalAndMentalHealth {

        private PsychologicalLiteracy psychologicalLiteracy;

        /**
         * 参加心理剧或心理月展示活动次数
         */
        private long countPsychodramaOrPsychoMonthShowcase;

        /**
         * 参与心理中心活动次数
         */
        private long countPsychologicalCenter;

        /**
         * 身体素养
         */
        private PhysicalLiteracy physicalLiteracy;

        @Data
        @Builder
        public static class PsychologicalLiteracy {
            /**
             * 参加心理剧或心理月展示活动次数
             */
            private long countPsychodramaOrPsychoMonthShowcase;

            /**
             * 参与心理中心活动次数
             */
            private long countPsychologicalCenter;

        }

        @Data
        @Builder
        public static class PhysicalLiteracy {
            /**
             * 国家级
             */
            private long countNationalLevel;

            /**
             * 市级
             */
            private long countMunicipalLevel;

            /**
             * 区（行业）级
             */
            private long countDistrictIndustryLevel;

            /**
             * 校级
             */
            private long schoolLevel;
        }
    }

    @Data
    @Builder
    public static class AestheticAndArtisticAccomplishment {

        /**
         * 参加才艺活动次数
         */
        private long countTalentActivities;

        /**
         * 参加学校及以上组织的艺术社团
         */
        private long countArtSocieties;

        /**
         * 国家级
         */
        private long countNationalLevel;

        /**
         * 市级
         */
        private long countMunicipalLevel;

        /**
         * 区（行业）级
         */
        private long countDistrictIndustryLevel;

        /**
         * 校级
         */
        private long schoolLevel;

        /**
         * 国家级记录
         */
        private List<String> countNationalLevelRecords;

        /**
         * 市级记录
         */
        private List<String> countMunicipalLevelRecords;

        /**
         * 区（行业）级记录
         */
        private List<String> countDistrictIndustryLevelRecords;

        /**
         * 校级记录
         */
        private List<String> schoolLevelRecords;
    }

    @Data
    @Builder
    public static class LaborAndProfessionalism {

        /**
         * 参加志愿者服务次数
         */
        private long countVolunteerService;

        /**
         * 参加交通岗值勤次数
         */
        private long countTrafficPostsAreOnDuty;

        /**
         * 志原者活动学分
         */
        private List<CreditCompletion> creditsForShiharaActivities;

        /**
         * 生产劳动实践学分
         */
        private List<CreditCompletion> productionLaborPracticeCredits;

        @Data
        @Builder
        public static class CreditCompletion {
            private Long semesterId;
            private String semesterName;
            private String completion;
        }

    }

}
