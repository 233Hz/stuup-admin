package com.poho.stuup.dao;


import com.poho.stuup.model.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TeacherMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    Teacher selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    /**
     * @param jobNo
     * @return
     */
    Teacher findTeacherByJobNo(String jobNo);

    /**
     * @param facultyId
     * @return
     */
    List<Teacher> findTeachers(Integer facultyId);

    /**
     * @param map
     * @return
     */
    int findTotalTeacherByCond(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Teacher> findTeacherPageResultByCond(Map<String, Object> map);

    /**
     * @param key
     * @return
     */
    List<Teacher> findTeacherByKey(String key);

    /**
     * @param map
     * @return
     */
    List<Teacher> findCourseTeacherByKey(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Teacher> findCourseTeacherByClass(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Teacher> findTeachersByGroup(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Teacher> findTeachersByGroupAdmin(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Teacher> findTeachersByFacultyAdmin(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Teacher> findTeachersByClassAdmin(Map<String, Object> map);

    /**
     * @param teacherName
     * @return
     */
    Teacher findTeacherByName(String teacherName);

    /**
     * @return
     */
    List<Teacher> findCourseTeachers();

    /**
     * @return
     */
    List<Teacher> findCourseTeachersByCond(Map<String, Object> map);

    Teacher findEvaScoreTeacher(Map<String, Object> map);

    List<Teacher> findCourseTeachersByTeacherId(Map<String, Object> map);

    List<Teacher> selectAll();

    List<Teacher> getAllTeacherJobNoAndName();

    String getJobNoById(@Param("id") Integer id);

    Integer getIdByJobNo(@Param("jobNo") String jobNo);

    List<Teacher> selectAllIdName();
}