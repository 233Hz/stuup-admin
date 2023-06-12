package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IStudentService {

    ResponseModel findDataPageResult(String grade, String major, String key, int page, int pageSize);

    /**
     * @description: 通过学号获取学生信息
     * @param: loginName
     * @return: com.poho.stuup.model.Student
     * @author BUNGA
     * @date: 2023/6/9 10:45
     */
    Student getStudentForStudentNO(@Param("studentNo") String studentNo);

    List<Integer> getAllStudentId();
}
