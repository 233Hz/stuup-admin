package com.poho.stuup.dao;


import com.poho.stuup.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface StudentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    /**
     * @param map
     * @return
     */
    Student selectByStudentNo(Map<String, Object> map);

    /**
     * @param classId
     * @return
     */
    int findTotalStudentByClass(Integer classId);

    /**
     * @param map
     * @return
     */
    int findTotalStudentByCond(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Student> findStudentPageResultByCond(Map<String, Object> map);

    List<Student> findAllStudent(Map<String, Object> map);

    /**
     * 查找学时id
     *
     * @param studentNo
     * @return
     */
    Long findStudentId(@Param("studentNo") String studentNo);

    Set<Long> selectIdList();

    /**
     * @description: 通过学号获取学生信息
     * @param: loginName
     * @return: com.poho.stuup.model.Student
     * @author BUNGA
     * @date: 2023/6/9 10:45
     */
    Student getStudentForStudentNO(@Param("studentNo") String studentNo);

    List<Student> getAllStudent();

    List<Integer> getAllStudentId();

    List<Long> findStudentUserIdByClassId(@Param("classIds") List<Integer> classIds);
}