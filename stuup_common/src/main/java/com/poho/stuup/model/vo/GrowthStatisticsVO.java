package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description
 * @date 2023/9/12 16:34
 */

@Data
public class GrowthStatisticsVO {

    private String yearName;
    private String semesterName;
    private String majorName;
    private String className;
    private String gradeName;
    private String classTeacher;
    private Long studentId;
    private String studentName;
    private String studentNo;
    private BigDecimal semesterScore;
    private Integer semesterRank;
    private Integer lastSemesterCompare;
    private BigDecimal compareResult;
    private Long applyCount;
    private BigDecimal totalScore;
    private Integer finalGrades;

    @JsonIgnore
    private Long yearId;

    @JsonIgnore
    private Long semesterId;

    @JsonIgnore
    private Integer majorId;

    @JsonIgnore
    private Integer classId;

    @JsonIgnore
    private Integer teacherId;

    @JsonIgnore
    private Integer gradeId;

}
