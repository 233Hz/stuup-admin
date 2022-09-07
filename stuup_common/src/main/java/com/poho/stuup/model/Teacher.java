package com.poho.stuup.model;

import java.util.Date;

public class Teacher {
    private Integer id;

    private String jobNo;

    private String name;

    private Integer sex;

    private Integer facultyId;

    private Integer teachGroup;

    private String phone;

    private String idCard;

    private String address;

    private Integer state;

    private String intro;

    private Date updateIntroDate;

    private String photoPath;

    private Integer isValid;

    private Integer invigilate;

    private String facultyName;

    /**
     * 导入数据所在行数
     */
    private Integer rowNum;
    /**
     * 教研组名称
     */
    private String groupName;
    private String courseName;
    private Integer standardId;
    private Integer score;
    private Integer teachCase;

    private Integer teachCommon;

    private Integer teachContent;

    private Integer teachMethod;

    private Integer teachEffect;
    private Integer standardItemId;
    private Integer scoreId;
    private Integer courseId;

    //教职工类别
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo == null ? null : jobNo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getTeachGroup() {
        return teachGroup;
    }

    public void setTeachGroup(Integer teachGroup) {
        this.teachGroup = teachGroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    public Date getUpdateIntroDate() {
        return updateIntroDate;
    }

    public void setUpdateIntroDate(Date updateIntroDate) {
        this.updateIntroDate = updateIntroDate;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath == null ? null : photoPath.trim();
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Integer getInvigilate() {
        return invigilate;
    }

    public void setInvigilate(Integer invigilate) {
        this.invigilate = invigilate;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getStandardId() {
        return standardId;
    }

    public void setStandardId(Integer standardId) {
        this.standardId = standardId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTeachCase() {
        return teachCase;
    }

    public void setTeachCase(Integer teachCase) {
        this.teachCase = teachCase;
    }

    public Integer getTeachCommon() {
        return teachCommon;
    }

    public void setTeachCommon(Integer teachCommon) {
        this.teachCommon = teachCommon;
    }

    public Integer getTeachContent() {
        return teachContent;
    }

    public void setTeachContent(Integer teachContent) {
        this.teachContent = teachContent;
    }

    public Integer getTeachMethod() {
        return teachMethod;
    }

    public void setTeachMethod(Integer teachMethod) {
        this.teachMethod = teachMethod;
    }

    public Integer getTeachEffect() {
        return teachEffect;
    }

    public void setTeachEffect(Integer teachEffect) {
        this.teachEffect = teachEffect;
    }

    public Integer getStandardItemId() {
        return standardItemId;
    }

    public void setStandardItemId(Integer standardItemId) {
        this.standardItemId = standardItemId;
    }

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}