package com.poho.stuup.service;


import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Teacher;

import java.util.List;
import java.util.Map;

public interface ITeacherService {

    int insert(Teacher record);

    int insertSelective(Teacher record);

    Teacher selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    /**
     * @param facultyId
     * @return
     */
    List<Teacher> findTeachers(Integer facultyId);

    ResponseModel findDataPageResult(Integer facultyId, String key, int page, int pageSize);


    /**
     * @param key
     * @return
     */
    List<Map<String, Object>> searchTeacher(String key);


    Map<Integer, Teacher> teacherMap();
}
