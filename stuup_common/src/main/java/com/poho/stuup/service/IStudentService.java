package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IStudentService {

    ResponseModel findDataPageResult(String grade, String major, String key, int page, int pageSize);

    Student selectByPrimaryKey(Integer id);

    /**
     * @description: 通过学号获取学生信息
     * @param: loginName
     * @return: com.poho.stuup.model.Student
     * @author BUNGA
     * @date: 2023/6/9 10:45
     */
    Student getStudentForStudentNO(@Param("studentNo") String studentNo);

    List<Integer> getAllStudentId();

    /**
     * @description: 统计在校生人数
     * @param:
     * @return: java.lang.Integer
     * @author BUNGA
     * @date: 2023/7/19 19:28
     */
    Integer countAtSchoolNum();

    /**
     * 通过学号获取学生id
     *
     * @param studentNo 学号
     * @return studentId 学生id
     */
    Long findIdByStudentNo(String studentNo);

    /**
     * 获取所有学生列表
     *
     * @return 学生列表
     */
    List<Student> getAllStudent();

    Long getIdByStudentNo(String studentNo);

    Integer getStudentIdByUserId(Long userId);

}
