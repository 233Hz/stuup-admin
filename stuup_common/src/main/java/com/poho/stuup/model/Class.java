package com.poho.stuup.model;

public class Class {
    /**
    * 唯一标识
    */
    private Integer id;

    /**
    * 序号
    */
    private Integer num;

    /**
     * 班号
     */
    private String code;

    /**
    * 班级名称
    */
    private String name;

    /**
    * 系部id
    */
    private Integer facultyId;

    private String facultyName;

    /**
    * 年级id
    */
    private Integer gradeId;

    private String gradeName;

    /**
    * 班主任
    */
    private Integer teacherId;

    /**
     * 管理员
     */
    private Integer adminId;

    /**
    * 人数
    */
    private Integer count;

    /**
     * 专业
     */
    private Integer majorId;
    private String majorName;

    /**
    * 是否有效:1、有效；0、无效
    */
    private Integer isValid;

    /**
     * 是否参与排考
     */
    private Integer joinExam;

    /**
     * 班主任姓名
     */
    private String teacherName;
    /**
     * 班主任工号
     */
    private String teacherNo;

    /**
     * 导入数据所在行数
     */
    private Integer rowNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public Integer getJoinExam() {
        return joinExam;
    }

    public void setJoinExam(Integer joinExam) {
        this.joinExam = joinExam;
    }
}