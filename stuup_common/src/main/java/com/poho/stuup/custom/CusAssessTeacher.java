package com.poho.stuup.custom;

/**
 * @Author wupeng
 * @Description 评价员工股数据模型
 * @Date 2020-10-13 14:57
 * @return
 */
public class CusAssessTeacher {
    private Long userId;
    private String userName;
    private String userType;
    private Long deptId;
    private String deptName;
    private Integer score;
    private Integer retire;
    private String note;

    public CusAssessTeacher() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getRetire() {
        return retire;
    }

    public void setRetire(Integer retire) {
        this.retire = retire;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}