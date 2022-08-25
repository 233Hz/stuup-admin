package com.poho.stuup.custom;

import java.util.List;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 17:58 2020/10/20
 * @Modified By:
 */
public class CusRangeSubmit {
    private Long yearId;
    private Integer userType;
    private Long deptId;
    private Long createUser;
    private List<Long> users;
    private Integer retire;
    private String note;

    public CusRangeSubmit() {

    }

    public Long getYearId() {
        return yearId;
    }

    public void setYearId(Long yearId) {
        this.yearId = yearId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
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
