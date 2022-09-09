package com.poho.stuup.service;

public interface ISynchronizeService {

    /**
     * 同步部门
     */
    void synchronizeDept();

    /**
     * 同步班级[已完成]
     */
    void synchronizeClass();
    /**
     * 同步教师
     */
    void synchronizeTeacher();

    /**
     * 同步学生
     */
    void synchronizeStudent();

    /**
     * 同步学期[已完成]
     */
    void synchronizeTerm();

    /**
     * 同步年级[已完成]
     */
    void synchronizeGrade();

    /**
     * 同步专业[已完成]
     */
    void synchronizeMajor();

    /**
     * 同步系部[已完成]
     */
    void synchronizeFaculty();

    /**
     * 同步教研组[已完成]
     */
    void synchronizeTeachGroup();

}
