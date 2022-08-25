package com.poho.stuup.custom;

import java.util.List;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 15:03 2020/10/13
 * @Modified By:
 */
public class CusTeacherSubmit {
    private Long yearId;
    private Integer assessType;
    private Long assessUser;
    private List<CusAssessTeacher> assessTeachers;
    private Integer state;

    public CusTeacherSubmit() {

    }

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public Integer getAssessType() {
        return assessType;
    }

    public void setAssessType(Integer assessType) {
        this.assessType = assessType;
    }

    public Long getAssessUser() {
        return assessUser;
    }

    public void setAssessUser(Long assessUser) {
        this.assessUser = assessUser;
    }

    public List<CusAssessTeacher> getAssessTeachers() {
        return assessTeachers;
    }

    public void setAssessTeachers(List<CusAssessTeacher> assessTeachers) {
        this.assessTeachers = assessTeachers;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
