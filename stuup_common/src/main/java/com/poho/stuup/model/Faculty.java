package com.poho.stuup.model;

import java.util.Date;

public class Faculty {
    private Integer oid;

    private String facultyName;

    private String facultyCode;

    private Integer facultyAdmin;

    private Date createTime;

    /**
     * 管理员姓名
     */
    private String adminName;

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName == null ? null : facultyName.trim();
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode == null ? null : facultyCode.trim();
    }

    public Integer getFacultyAdmin() {
        return facultyAdmin;
    }

    public void setFacultyAdmin(Integer facultyAdmin) {
        this.facultyAdmin = facultyAdmin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}