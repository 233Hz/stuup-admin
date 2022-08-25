package com.poho.stuup.custom;

import java.util.List;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 22:03 2020/9/20
 * @Modified By:
 */
public class CusAssessSubmit {
    private Long yearId;
    private Integer assessType;
    private Long assessUser;
    private List<CusAssessUser> assessUsers;
    private Integer state;

    public CusAssessSubmit () {

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

    public List<CusAssessUser> getAssessUsers() {
        return assessUsers;
    }

    public void setAssessUsers(List<CusAssessUser> assessUsers) {
        this.assessUsers = assessUsers;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
