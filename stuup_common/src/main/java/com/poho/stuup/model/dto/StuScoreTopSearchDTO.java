package com.poho.stuup.model.dto;


import com.poho.stuup.constant.ProjectConstants;

import java.io.Serializable;

public class StuScoreTopSearchDTO implements Serializable {

    private Integer top;

    private Integer gradeId;

    private Integer classId;

    private Integer majorId;

    public Integer getTop() {
        if(top == null || top < 1) {
           return ProjectConstants.STU_SCORE_TOP_DEFAULT;
        }
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }
}
