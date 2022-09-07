package com.poho.stuup.dao;


import com.poho.stuup.model.Student;

import java.util.List;
import java.util.Map;

public interface StudentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    /**
     *
     * @param map
     * @return
     */
    Student selectByStudentNo(Map<String, Object> map);

    /**
     *
     * @param classId
     * @return
     */
    int findTotalStudentByClass(Integer classId);

    /**
     *
     * @param map
     * @return
     */
    int findTotalStudentByCond(Map<String, Object> map);

    /**
     *
     * @param map
     * @return
     */
    List<Student> findStudentPageResultByCond(Map<String, Object> map);

    List<Student> findAllStudent(Map<String, Object> map);
}